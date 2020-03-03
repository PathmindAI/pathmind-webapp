#!/usr/bin/python
import os
import sys
import boto3
import json
import sh
import traceback
import psycopg2
import time
from LoggerInit import LoggerInit
from threading import Thread
import subprocess

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
        app_logger.error(sql_string.replace('\n',' '))
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

def send_mockup_data(s3bucket, s3path, cycle):
    """
    sends the mockup data to the s3 bucket every cycle
    """
    global mockup_status
    folder_list=set()
    src_bucket=ENVIRONMENT+"-training-static-files.pathmind.com"
    s3 = boto3.resource('s3')
    my_bucket = s3.Bucket(src_bucket)
    for my_bucket_object in my_bucket.objects.filter(Prefix='mockup/', Delimiter=''):
        folder_list.add(int(my_bucket_object.key.split('/')[1]))
    folder_list=sorted(folder_list)
    for folder in folder_list:
        if mockup_status[s3bucket+'/'+s3path]['destroy']==True:
            app_logger.info('Killing mockup {s3bucket}/{s3path}'\
                .format(s3bucket=s3bucket,s3path=s3path))
            return True
        app_logger.info('Sending mockup folder {folder} to {s3bucket}/{s3path}'\
            .format(folder=folder,s3bucket=s3bucket,s3path=s3path))
        for my_bucket_object in my_bucket.objects.filter(
            Prefix='mockup/'+str(folder)+'/', Delimiter=''):

            key=my_bucket_object.key
            copy_source = { 'Bucket': src_bucket, 'Key': key }
            target_key='/'.join(key.split('/')[2:])
            s3.meta.client.copy(copy_source, s3bucket, s3path+'/output/'+target_key)
        time.sleep(cycle)

def process_message(message):
    """
    parses and creates the spot instance for the sqs message received
    """
    if not message:
        return
    global update_cluster
    global mockup_status
    app_logger.info('Received {message}'.format(message=message['Body']))
    body=json.loads(message['Body'])
    s3bucket=body['S3Bucket']
    s3path=body['S3Path']
    job_id=s3path
    ReceiptHandle=message['ReceiptHandle']

    #jobs is done so destroy the spot instance and the pod
    if 'destroy' in body:
        app_logger.info('Destroying s3://{s3bucket}/{s3path}'\
                .format(s3bucket=s3bucket,
                        s3path=s3path))
        if 'mockup' in body:
           mockup_status[s3bucket+'/'+s3path]={'destroy':True}
        else:
            sql_script="""
                    update public.trainer_job
                    set status=6,
                    ec2_end_date=NOW(),
                    update_date=NOW()
                    where job_id='{job_id}'
            """.format(job_id=job_id)
            execute_psql(sql_script)
            try:
                if int(body['destroy']) == 1:
                    app_logger.info('Uploading killed file for {job_id}'\
                        .format(job_id=job_id))
                    open('killed', 'w').close()
                    s3 = boto3.client('s3')
                    s3.upload_file('killed', \
                        s3bucket, \
                        s3path+'/output/killed')
                app_logger.info('Deleting deployment {job_id}'.format(job_id=job_id))
                sh.kubectl('delete','deployment',job_id)
                app_logger.info('Deleting ig {job_id}'.format(job_id=job_id))
                sh.kops('delete','ig',job_id,'--yes')
            except Exception as e:
                app_logger.error(traceback.format_exc())

    elif 'mockup' in body:
        app_logger.info('Sending dummy data to s3://{s3bucket}/{s3path}'\
                .format(s3bucket=s3bucket,
                        s3path=s3path))
        if 'cycle' in body:
            cycle=int(body['cycle'])
        else:
            cycle=60
        worker = Thread(target=send_mockup_data, args=(s3bucket,s3path,cycle,))
        worker.setDaemon(True)
        worker.start()
        mockup_status[s3bucket+'/'+s3path]={'destroy':False}

    else:
        #create node and deployment
        ec2_instance_type='m5.2xlarge'
        ec2_max_price='0.3'

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
                if ENVIRONMENT=='prod':
                    NAMESPACE='default'
                else:
                    NAMESPACE=ENVIRONMENT
                line=line.replace('{{NAMESPACE}}',NAMESPACE)
                line=line.replace('{{ENVIRONMENT}}',ENVIRONMENT)
                line=line.replace('{{SQS_URL}}',SQS_URL)
                file.write(line+'\n')

        if 'retry' in body:
            #Update the description
            sql_script="""
                update public.trainer_job
                set description='spot instance creation failed, trying again',
                update_date=NOW()
                where job_id='{job_id}'
            """.format(job_id=job_id)
        else:
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
                status,
                description)
                VALUES (
                '{job_id}',
                '{SQS_URL}',
                '{s3path}',
                '{s3bucket}',
                '{ReceiptHandle}',
                '{ec2_instance_type}',
                '{ec2_max_price}',
                {status},
                'pathmind training job')
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

        #Create spot ig and deployment
        try:
            app_logger.info('Creating ig {job_id}'.format(job_id=job_id))
            sh.kops('create','-f',JOB_IG_FILE)
            app_logger.info('Creating deployment {job_id}'.format(job_id=job_id))
            sh.kubectl('apply','-f',JOB_DEPLOYMENT_FILE)
            update_cluster=True
        except Exception as e:
            app_logger.error(traceback.format_exc())

    #Delete message
    sqs = boto3.client('sqs')
    response = sqs.delete_message(
        QueueUrl=SQS_URL,
        ReceiptHandle=ReceiptHandle
    )


def check_ig_status():
    """
    Checks is all ig are created and have nodes associated
    """
    sqs = boto3.client('sqs')

    while True:
        app_logger.info('Checking ig status')
        p = subprocess.Popen(['kops','validate','cluster',NAME], stdout=subprocess.PIPE)
        output='\n'.join([i for i in p.communicate() if i is not None]).split('\n')
        for line in output:
            if 'did not have enough nodes' in line:
                job_id=line.split('\t')[1]
                #Get the bucket id
                try:
                    psql_connection = psycopg2.connect(user = psql_con_details['user'],
                        password = psql_con_details['password'],
                        host = psql_con_details['host'],
                        port = psql_con_details['port'],
                        database = psql_con_details['dbname'])
                    psql_cursor = psql_connection.cursor()
                    sql_script="""
                      select s3bucket from public.trainer_job where job_id='{job_id}'
                    """.format(job_id=job_id)
                    psql_cursor.execute(sql_script)
                    rows = psql_cursor.fetchall()
                    s3bucket=rows[0][0]
                except Exception as e:
                    app_logger.error(traceback.format_exc())
                    app_logger.error(sql_string.replace('\n',' '))
                psql_cursor.close()
                psql_connection.close()
                #Delete deployment and ig
                try:
                    app_logger.info('Deleting deployment {job_id}'.format(job_id=job_id))
                    sh.kubectl('delete','deployment',job_id)
                    app_logger.info('Deleting ig {job_id}'.format(job_id=job_id))
                    sh.kops('delete','ig',job_id,'--yes')
                except Exception as e:
                    app_logger.error(traceback.format_exc())
                #send sqs message again
                message='{"S3Bucket": "'+s3bucket+'", "S3Path":"'+job_id+'", "retry":"1"}'
                response = sqs.send_message(
                    QueueUrl=SQS_URL,
                    MessageBody=message,
                    MessageGroupId='training'
                )
        time.sleep(20)


def main():
    """
    main function listens on sqs queue defined in SQS_URL env variable
    """
    global update_cluster
    sqs = boto3.client('sqs')
    while True:
        app_logger.info('Waiting for messages in {SQS_URL}'\
            .format(SQS_URL=SQS_URL))
        resp = sqs.receive_message(
            QueueUrl=SQS_URL,
            AttributeNames=['All'],
            MaxNumberOfMessages=10,
            VisibilityTimeout=60,
            WaitTimeSeconds=20
        )
        if ('Messages' in resp):
            for message in resp['Messages']:
                process_message(message)
            if update_cluster:
                app_logger.info('Updating cluster')
                sh.kops('update','cluster',NAME,'--yes')
            update_cluster=False


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
    mockup_status={}
    mem_ig_template=[]
    mem_deployment_template=[]
    update_cluster=False
    logger=LoggerInit()
    app_logger=logger.get_logger("trainer")
    #Init kubectl
    sh.kops('export','kubecfg','--name',NAME)
    #Main thread
    load_ig_template()
    load_deployment_template()
    parse_dburl()
    worker1 = Thread(target=main, args=())
    worker1.setDaemon(True)
    worker1.start()
    #check ig thread
    #worker2 = Thread(target=check_ig_status, args=())
    #worker2.setDaemon(True)
    #worker2.start()
    worker1.join()
    #worker2.join()

