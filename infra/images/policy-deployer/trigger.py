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
    S3ModelPath=body['S3ModelPath']
    S3SchemaPath=body['S3SchemaPath']
    JobId=body['JobId']
    helm_name="policy-"+JobId
    domain_name="devpathmind.com"
    ReceiptHandle=message['ReceiptHandle']
    tag=ENVIRONMENT+JobId

    #jobs is done so destroy the spot instance and the pod
    if 'destroy' in body:
        app_logger.info('Destroying s3://{s3bucket}/{JobId}'\
                .format(s3bucket=s3bucket,
                        JobId=JobId))
        try:
            app_logger.info('Deleting helm {helm_name}'.format(helm_name=helm_name))
            sh.helm('delete',helm_name,'-n',NAMESPACE)
        except Exception as e:
            app_logger.error(traceback.format_exc())
    else:
        policyServerStatus=2
        try:
            #insert the status to run
            sql_script=""" 
                update public.run set policyServerStatus=1 where job_id='{JobId}'
            """.format(JobId=JobId)
            execute_psql(sql_script)
            app_logger.info('Clonning repository')
            sh.mkdir('-p','policy-server')
            sh.rm('-rf','policy-server')
            sh.git('clone','git@github.com:SkymindIO/policy-server.git')
            app_logger.info('Creating container')
            output=sh.bash('build_and_push.sh'\
                ,'policy-server'\
                ,'policy-server'\
                ,'{tag}'.format(tag=tag)\
                ,'--build-arg', 'S3BUCKET={s3bucket}'.format(s3bucket=s3bucket) \
                ,'--build-arg', 'S3MODELPATH={S3ModelPath}'.format(S3ModelPath=S3ModelPath) \
                ,'--build-arg', 'S3SCHEMAPATH={S3SchemaPath}'.format(S3SchemaPath=S3SchemaPath) \
                ,'--build-arg', 'AWS_SECRET_ACCESS_KEY={AWS_SECRET_ACCESS_KEY}'.format(AWS_SECRET_ACCESS_KEY=AWS_SECRET_ACCESS_KEY) \
                ,'--build-arg', 'AWS_ACCESS_KEY_ID={AWS_ACCESS_KEY_ID}'.format(AWS_ACCESS_KEY_ID=AWS_ACCESS_KEY_ID))
            if output.exit_code != 0:
                policyServerStatus=3
            else:
                app_logger.info('Creating helm {helm_name}'.format(helm_name=helm_name))
                output=sh.helm('upgrade' \
                    ,'--install' \
                    ,helm_name \
                    ,'policy-server/helm/policy-server/' \
                    ,'--set', 'image.tag={ENVIRONMENT}{JobId}'.format(ENVIRONMENT=ENVIRONMENT,JobId=JobId) \
                    ,'--set', '\'ingress.hosts[0].host\'=\'{JobId}.{ENVIRONMENT}.{domain_name}\''.format(ENVIRONMENT=ENVIRONMENT,JobId=JobId,domain_name=domain_name) \
                    ,'--set', '\'ingress.hosts[0].paths[0]\'=\'/\'' \
                    ,'--set', '\'ingress.tls[0].hosts[0]\'=\'{JobId}.{ENVIRONMENT}.{domain_name}\''.format(ENVIRONMENT=ENVIRONMENT,JobId=JobId,domain_name=domain_name) \
                    ,'--set', '\'ingress.tls[0].secretName\'=\'letsencrypt-{ENVIRONMENT}\''.format(ENVIRONMENT=ENVIRONMENT) \
                    ,'-n',NAMESPACE)
                if output.exit_code != 0:
                    policyServerStatus=3
        except Exception as e:
            policyServerStatus=3
            app_logger.error(traceback.format_exc())

        #insert the status to run
        sql_script=""" 
            update public.run set policyServerStatus={policyServerStatus} where job_id='{JobId}'
        """.format(JobId=JobId,policyServerStatus=policyServerStatus)
        execute_psql(sql_script)

    try:
        #Delete message
        sqs = boto3.client('sqs')
        response = sqs.delete_message(
            QueueUrl=SQS_URL,
            ReceiptHandle=ReceiptHandle
        )
    except Exception as e:
        app_logger.error(traceback.format_exc())

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
    AWS_SECRET_ACCESS_KEY=os.environ['AWS_SECRET_ACCESS_KEY']
    AWS_ACCESS_KEY_ID=os.environ['AWS_ACCESS_KEY_ID']
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
