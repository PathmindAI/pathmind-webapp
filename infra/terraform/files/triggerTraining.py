import json
import boto3

REGION = 'us-east-1' 
AMI = 'ami-00068cd7555f543d5'
INSTANCE_TYPE = 't2.micro'
EC2 = boto3.client('ec2', region_name=REGION)


def lambda_handler(event, context):
    message = 'TEST'

    # bash script to run:
    #  - update and install httpd (a webserver)
    #  - start the webserver
    #  - create a webpage with the provided message.
    #  - set to shutdown the instance in 5 minutes.
    init_script = """#!/bin/bash
yum update -y
yum install -y httpd24
service httpd start
chkconfig httpd on
echo """ + message + """ > /var/www/html/index.html
shutdown -h +5"""
    
    print('Running script:')
    print(init_script)

    instance = EC2.run_instances(
        ImageId=AMI,
        InstanceType=INSTANCE_TYPE,
        MinCount=1, 
        MaxCount=1,
        InstanceInitiatedShutdownBehavior='terminate', 
        UserData=init_script 
    )

    print("New instance created.")
    instance_id = instance['Instances'][0]['InstanceId']
    print(instance_id)

    
    return {
        'statusCode': 200,
        'body': json.dumps(instance_id)
    }

