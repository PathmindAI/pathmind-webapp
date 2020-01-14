#!/usr/bin/python
import os
import sys
import boto3
import json
import sh
import traceback
import psycopg2
from LoggerInit import LoggerInit

def parse_dburl():
    """
    Reads DBURL_CLI env variable and creates a dict with conneciton details
    """
    global psql_con_details
    DB_URL_CLI=os.environ['DB_URL_CLI']
    for param in DB_URL_CLI.split(' '):
        key=param.split('=')[0]
        value=param.split('=')[1]
        psql_con_details[key]=value

def execute_psql(sql_string):
    """
    creates a psql connection ans excutes the given sql string
    """
    try:
        psql_connection = psycopg2.connect(user = psql_con_details['user'],
            password = psql_con_details['password'],
            host = psql_con_details['host'],
            port = psql_con_details['port'],
            database = psql_con_details['dbname'])
        psql_cursor = psql_connection.cursor()
        psql_cursor.execute(sql_string)
        psql_connection.commit()
    except Exception as e:
        app_logger.error(traceback.format_exc())
        app_logger.error(sql_string.replace('\n',' ')
    psql_cursor.close()
    psql_connection.close()


def load_ig_template():
    """
    loads the spot ig file into memory
    """
    global mem_ig_template
    with open(IG_TEMPLATE,'r') as file:
        mem_ig_template=file.read().split('\n')

def load_deployment_template():
    """
    loads the deployment yaml file into memory
    """
    global mem_deployment_template
    with open(DEPLOYMENT_TEMPLATE,'r') as file:
        mem_deployment_template=file.read().split('\n')

def process_message(message):
    """
    parses and creates the spot instance for the sqs message received
    """
    if not message:
        return
    body=json.loads(message['Messages'][0]['Body'])
    s3bucket=body['S3Bucket']
    s3path=body['S3Path']
    job_id=s3path
    ReceiptHandle=message['Messages'][0]['ReceiptHandle']

    #jobs is done so destroy the spot instance and the pod
    if 'destroy' in body:
        app_logger.info('Destroying s3://{s3bucket}/{s3path}'\
                .format(s3bucket=s3bucket,
                        s3path=s3path))
        sql_script="""
                update public.trainer_job set status=5 where job_id='{job_id}'
        """.format(job_id=job_id)
        execute_psql(sql_script)
        try:
            app_logger.info('Deleting deployment {job_id}'.format(job_id=job_id))
            sh.kubectl('delete','deployment',job_id)
            app_logger.info('Deleting ig {job_id}'.format(job_id=job_id))
            sh.kops('delete','ig',job_id,'--yes')
        except Exception as e:
            app_logger.error(traceback.format_exc())
        #Delete message
        sqs = boto3.client('sqs')
        response = sqs.delete_message(
            QueueUrl=SQS_URL,
            ReceiptHandle=ReceiptHandle
        )
        return True

    #create node and deployment
    ec2_instance_type='t2.2xlarge'
    ec2_max_price='0.15'

    app_logger.info('Starting job s3://{s3bucket}/{s3path}'\
            .format(s3bucket=s3bucket,
                s3path=s3path))

    #replace values in template an dcreate spot ig yaml file
    JOB_IG_FILE=job_id+"_"+IG_FILE
    with open(JOB_IG_FILE,'w') as file:
        for line in mem_ig_template:
            line=line.replace('{{CLUSTER_NAME}}',NAME)
            line=line.replace('{{IG_NAME}}',job_id)
            line=line.replace('{{INSTANCE_TYPE}}',ec2_instance_type)
            line=line.replace('{{MAX_PRICE}}',ec2_max_price)
            file.write(line+'\n')

    #replace values in template an dcreate deployment yaml file
    JOB_DEPLOYMENT_FILE=job_id+"_"+DEPLOYMENT_FILE
    with open(JOB_DEPLOYMENT_FILE,'w') as file:
        for line in mem_deployment_template:
            line=line.replace('{{IMAGE}}',RL_IMAGE)
            line=line.replace('{{S3BUCKET}}',s3bucket)
            line=line.replace('{{S3PATH}}',s3path)
            line=line.replace('{{JOB_ID}}',job_id)
            line=line.replace('{{ENVIRONMENT}}',ENVIRONMENT)
            line=line.replace('{{SQS_URL}}',SQS_URL)
            file.write(line+'\n')

    #insert the status to trainer_job
    sql_script="""
        INSERT INTO public.trainer_job (
        job_id,
        sqs_url,
        s3path,
        s3bucket,
        receipthandle,
        ec2_instance_type,
        ec2_max_price,
        status)
        VALUES (
        '{job_id}',
        '{SQS_URL}',
        '{s3path}',
        '{s3bucket}',
        '{ReceiptHandle}',
        '{ec2_instance_type}',
        '{ec2_max_price}',
        {status})
    """.format(
        job_id=job_id,
        SQS_URL=SQS_URL,
        s3path=s3path,
        s3bucket=s3bucket,
        ReceiptHandle=ReceiptHandle,
        ec2_instance_type=ec2_instance_type,
        ec2_max_price=ec2_max_price,
        status=1)
    execute_psql(sql_script)

    #Delete message
    sqs = boto3.client('sqs')
    response = sqs.delete_message(
        QueueUrl=SQS_URL,
        ReceiptHandle=ReceiptHandle
    )

    #Create spot ig and deployment
    try:
        app_logger.info('Creating ig {job_id}'.format(job_id=job_id))
        sh.kops('create','-f',JOB_IG_FILE)
        app_logger.info('Updating cluster')
        sh.kops('update','cluster',NAME,'--yes')
        app_logger.info('Creating deployment {job_id}'.format(job_id=job_id))
        sh.kubectl('apply','-f',JOB_DEPLOYMENT_FILE)
    except Exception as e:
        app_logger.error(traceback.format_exc())


def main():
    """
    main function listens on sqs queue defined in SQS_URL env variable
    """
    load_ig_template()
    load_deployment_template()
    parse_dburl()
    sqs = boto3.client('sqs')
    while True:
        app_logger.info('Waiting for messages in {SQS_URL}'\
            .format(SQS_URL=SQS_URL))
        resp = sqs.receive_message(
            QueueUrl=SQS_URL,
            AttributeNames=['All'],
            MaxNumberOfMessages=1,
            VisibilityTimeout=60,
            WaitTimeSeconds=20
        )
        if ('Messages' in resp):
            process_message(resp)


if __name__ == "__main__":
    #Get env variables
    SQS_URL=os.environ['SQS_URL']
    NAME=os.environ['NAME']
    ENVIRONMENT=os.environ['ENVIRONMENT']
    RL_IMAGE=os.environ['RL_IMAGE']
    IG_TEMPLATE="spot_ig_template.yaml"
    DEPLOYMENT_TEMPLATE="rl_training_deployment.yaml"
    IG_FILE=IG_TEMPLATE.replace('_template','')
    DEPLOYMENT_FILE=DEPLOYMENT_TEMPLATE.replace('_template','')
    psql_con_details={}
    mem_ig_template=[]
    mem_deployment_template=[]
    logger=LoggerInit()
    app_logger=logger.get_logger("trainer")
    main()

