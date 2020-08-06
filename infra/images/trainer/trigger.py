#!/usr/bin/python
import os
import sys
import boto3
import json
import requests
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

def convert_to_seconds(s):
    """
    Convert time string expressed as <number>[m|h|d|s|w] to seconds
    """
    seconds_per_unit = {"s": 1, "m": 60, "h": 3600, "d": 86400, "w": 604800}
    return int(s[:-1]) * seconds_per_unit[s[-1]]

def post_message_to_slack(text):
    slack_url='https://hooks.slack.com/services/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'
    slack_channel = '#training-errors'
    slack_icon_url = 'https://a.slack-edge.com/production-standard-emoji-assets/10.2/google-medium/274c@2x.png'
    slack_user_name = 'Pathmind Trainings'
    return requests.post(slack_url, {
        'channel': slack_channel,
        'text': text,
        'icon_url': slack_icon_url,
        'username': slack_user_name,
    }).json()

def load_deployment_template(DEPLOYMENT_TEMPLATE):
    """
    loads the deployment yaml file into memory
    """
    global mem_deployment_template
    with open(DEPLOYMENT_TEMPLATE,'r') as file:
        mem_deployment_template=file.read().split('\n')
    return mem_deployment_template

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
            app_logger.info('Uploading killed file for {s3path}'\
                .format(s3path=s3path))
            open('killed', 'w').close()
            s3 = boto3.client('s3')
            s3.upload_file('killed', \
                s3bucket, \
                s3path+'/output/killed')
            return True
        app_logger.info('Sending mockup folder {folder} to {s3bucket}/{s3path}'\
            .format(folder=folder,s3bucket=s3bucket,s3path=s3path))
        for my_bucket_object in my_bucket.objects.filter(
            Prefix='mockup/'+str(folder)+'/', Delimiter=''):

            key=my_bucket_object.key
            copy_source = { 'Bucket': src_bucket, 'Key': key }
            target_key='/'.join(key.split('/')[2:])
            #s3.meta.client.copy(copy_source, s3bucket, s3path+'/output/'+target_key)
            s3.Object(s3bucket, s3path+'/output/'+target_key).put(Metadata={'key':src_bucket+'/'+key})
        time.sleep(cycle)

def th_monitor_job(message):
    """
    monitors if the pods for related to trainings are in Running mode
    """
    reported={}
    start_threshold=900
    while:
        data=sh.kubectl('get','pods',\
            '-n',NAMESPACE,\
            '--field-selector','status.phase=Running',\
            '-l','type=training')
        for line in data.split('\n')[1:]:
            pod=line.split()[0]
            if pod not in reported and pod:
                status=line.split()[2]
                age=line.split()[4]
                seconds=convert_to_seconds(age)
                if age>start_threshold:
                    text="Training {pod} is not able to start\
                        \nStatus: {status}\
                        \nElapsed Time: {age}\
                        \nEnvironment: {ENVIRONMENT}"\
                        .format(pod=pod,\
                            status=status,\
                            age=age,\
                            ENVIRONMENT=ENVIRONMENT)
                    post_message_to_slack(text)
                reported[pod]=1
        time.sleep(60)

def process_message(message):
    """
    parses and creates the spot instance for the sqs message received
    """
    if not message:
        return
    global mockup_status
    hw_type_list=['16cpu_32gb','16cpu_64gb','8cpu_16gb','8cpu_32gb','36cpu_72gb']
    app_logger.info('Received {message}'.format(message=message['Body']))
    body=json.loads(message['Body'])
    s3bucket=body['S3Bucket']
    s3path=body['S3Path']
    hw_type='36cpu_72gb'
    if 'hw_type' in body:
        hw_type=body['hw_type']
    if hw_type not in hw_type_list:
        app_logger.info('hw type {hw_type} not found using default'\
            .format(hw_type=hw_type))
        hw_type='16cpu_32gb'
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
        ec2_instance_type='TBD'
        ec2_max_price='0.3'

        app_logger.info('Starting job s3://{s3bucket}/{s3path}'\
                .format(s3bucket=s3bucket,
                    s3path=s3path))

        #replace values in template an dcreate deployment yaml file
        DEPLOY_TEMPLATE="rl_training_deployment-{hw_type}.yaml".format(hw_type=hw_type)
        mem_deployment_template=load_deployment_template(DEPLOY_TEMPLATE)
        JOB_DEPLOYMENT_FILE=job_id+"_"+DEPLOYMENT_FILE
        with open(JOB_DEPLOYMENT_FILE,'w') as file:
            for line in mem_deployment_template:
                line=line.replace('{{IMAGE}}',RL_IMAGE)
                line=line.replace('{{S3BUCKET}}',s3bucket)
                line=line.replace('{{S3PATH}}',s3path)
                line=line.replace('{{JOB_ID}}',job_id)
                line=line.replace('{{NAMESPACE}}',NAMESPACE)
                line=line.replace('{{ENVIRONMENT}}',ENVIRONMENT)
                line=line.replace('{{SQS_URL}}',SQS_URL)
                line=line.replace('{{NAME}}',NAME)
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
                'N/A',
                'N/A',
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

        #Create deployment
        try:
            app_logger.info('Creating deployment {job_id}'.format(job_id=job_id))
            sh.kubectl('apply','-f',JOB_DEPLOYMENT_FILE)
        except Exception as e:
            app_logger.error(traceback.format_exc())

    #Delete message
    sqs = boto3.client('sqs')
    response = sqs.delete_message(
        QueueUrl=SQS_URL,
        ReceiptHandle=ReceiptHandle
    )


def main():
    """
    main function listens on sqs queue defined in SQS_URL env variable
    """
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

if __name__ == "__main__":
    #Get env variables
    SQS_URL=os.environ['SQS_URL']
    NAME=os.environ['NAME']
    ENVIRONMENT=os.environ['ENVIRONMENT']
    if ENVIRONMENT=='prod':
        NAMESPACE='default'
    else:
        NAMESPACE=ENVIRONMENT
    RL_IMAGE=os.environ['RL_IMAGE']
    DEPLOYMENT_TEMPLATE="rl_training_deployment.yaml"
    DEPLOYMENT_FILE=DEPLOYMENT_TEMPLATE.replace('_template','')
    psql_con_details={}
    mockup_status={}
    logger=LoggerInit()
    app_logger=logger.get_logger("trainer")
    #Main thread
    parse_dburl()
    worker1 = Thread(target=main, args=())
    worker1.setDaemon(True)
    worker1.start()
    #start monitoring
    worker2 = Thread(target=th_monitor_job, args=())
    worker2.setDaemon(True)
    worker2.start()
    worker1.join()
    worker2.join()

