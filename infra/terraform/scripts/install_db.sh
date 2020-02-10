#!/usr/bin/bash

if [ "$1" == "" ] || [ "$2" == "" ] || [ "$3" == "" ] || [ "$4" == "" ] || [ "$5" == "" ] || [ "$6" == "" ] || [ "$7" == "" ]
then
	echo "Usage install_db.sh <db identifier> <s3 bucket> <s3 file> <db password> <aws_access_key_id> <aws_secret_access_key_id> <cluster name>"
	echo "Example install_db.sh dev-database pathmind-db pathmind_test.dump.gz Asdf1234 xxx xxx dev-pathmind.k8s.local"
	exit -2
fi

db_identifier=$1
s3_bucket=$2
s3_file=$3
dbpassword=$4
aws_access_key_id=$5
aws_secret_access_key_id=$6
NAME=$7
tmp_dir="/tmp/"
ENV=`echo ${NAME} | cut -f1 -d'.'`
ELB=`aws elb --output=table describe-load-balancers|grep DNSName.\*bastion|awk '{print $4}' | grep ${ENV}`

#Get RDS endpoint
endpoint=`aws rds describe-db-instances --db-instance-identifier ${db_identifier} --query "DBInstances[*].Endpoint.Address" | jq -r '.[0]'`
dburl="--host=$endpoint --port=5432 --dbname=pathminddb --user=pathmind"

ssh admin@${ELB} sudo apt-get update
ssh admin@${ELB} sudo apt-get install postgresql -y
ssh admin@${ELB} "printf '%s\n%s\nus-east-1\njson' 'AKIA4G2DQUWTNYKNB75F' 'AE55Akjz1NfuhRMEBaul5/byPbJz8FmN9xgl9WZe' | aws configure"
ssh admin@${ELB} aws s3 cp s3://${s3_bucket}/${s3_file} $tmp_dir
ssh admin@${ELB} gunzip -f ${tmp_dir}/${s3_file}
s3_file=`echo ${s3_file} | sed 's/.gz$//g'`
ssh admin@${ELB} "export PGPASSWORD='${dbpassword}'; pg_restore ${dburl} ${tmp_dir}/${s3_file}"
