#!/usr/bin/python
import os
import sys
import boto3
import json
import sh
import traceback
import logging
import psycopg2

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
    ReceiptHandle=(message['Messages'][0]['ReceiptHandle']

    #replace values in template an dcreate spot ig yaml file
    JOB_IG_FILE=job_id+"_"+IG_FILE
    with open(JOB_IG_FILE,'w') as file:
        for line in mem_ig_template:
            line=line.replace('{{CLUSTER_NAME}}',NAME)
            line=line.replace('{{IG_NAME}}',job_id)
            line=line.replace('{{INSTANCE_TYPE}}','t2.2xlarge')
            line=line.replace('{{MAX_PRICE}}','0.15')
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
            file.write(line+'\n')

    #Delete message
    response = client.delete_message(
        QueueUrl=SQS_URL
        ReceiptHandle=ReceiptHandle
    )

    #Create spot ig and deployment
    try:
        sh.kops('create','-f',JOB_IG_FILE)
        sh.kops('update','cluster',NAME,'--yes')
        sh.kubectl('apply','-f',JOB_DEPLOYMENT_FILE)
    except Exception as e:
        logging.error(traceback.format_exc())


def main():
    """
    main function listens on sqs queue defined in SQS_URL env variable
    """
    load_ig_template()
    load_deployment_template()
    sqs = boto3.client('sqs')
    resp = sqs.receive_message(
        QueueUrl=SQS_URL,
        AttributeNames=['All'],
        MaxNumberOfMessages=1,
        VisibilityTimeout=60,
        WaitTimeSeconds=20
    )
    if (resp):
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
    mem_ig_template=[]
    mem_deployment_template=[]
    main()

