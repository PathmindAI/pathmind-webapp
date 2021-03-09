#!/usr/bin/python3
import os
import sys
import boto3
import json
import requests
import sh
import traceback
import psycopg2
import time
import re
from datetime import timedelta
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
    UNITS = {'s':'seconds', 'm':'minutes', 'h':'hours', 'd':'days', 'w':'weeks'}
    return int(timedelta(**{
        UNITS.get(m.group('unit').lower(), 'seconds'): int(m.group('val'))
        for m in re.finditer(r'(?P<val>\d+)(?P<unit>[smhdw]?)', s, flags=re.I)
    }).total_seconds())

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

def process_message(message):
    """
    parses and creates the spot instance for the sqs message received
    """
    if not message:
        return
    app_logger.info('Received {message}'.format(message=message['Body']))
    body=json.loads(message['Body'])
    s3bucket=body['S3Bucket']
    s3path=body['S3Path']
    job_id=s3path
    helm_name="policy_"+job_id
    ReceiptHandle=message['ReceiptHandle']

    #jobs is done so destroy the spot instance and the pod
    if 'destroy' in body:
        app_logger.info('Destroying s3://{s3bucket}/{s3path}'\
                .format(s3bucket=s3bucket,
                        s3path=s3path))
        sql_script="""
                update public.trainer_job
                set status=6,
                ec2_end_date=NOW(),
                update_date=NOW()
                where job_id='{job_id}'
        """.format(job_id=job_id)
        execute_psql(sql_script)
        try:
            app_logger.info('Deleting helm {helm_name}'.format(helm_name=helm_name))
            sh.helm('delete',helm_name,´'-n',NAMESPACE)
        except Exception as e:
            app_logger.error(traceback.format_exc())
    else:
        try:
            app_logger.info('Creating helm {helm_name}'.format(helm_name=helm_name))
            sh.helm('upgrade','--install',helm_name,´'-n',NAMESPACE)
        except Exception as e:
            app_logger.error(traceback.format_exc())

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
#        execute_psql(sql_script)

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
    ENVIRONMENT=os.environ['ENVIRONMENT']
    if ENVIRONMENT=='prod':
        NAMESPACE='default'
    else:
        NAMESPACE=ENVIRONMENT
    psql_con_details={}
    logger=LoggerInit()
    app_logger=logger.get_logger("trainer")
    #Main thread
    parse_dburl()
    worker1 = Thread(target=main, args=())
    worker1.setDaemon(True)
    worker1.start()
    worker1.join()
