echo "Init start"

echo "configure aws"
# https://docs.aws.amazon.com/cli/latest/topic/s3-config.html
aws configure set default.s3.addressing_style path

sleep 10 # todo: not sure how long should be sleeping; just an arbitrary number of seconds to let localstack container started

aws --endpoint-url http://localstack:4566 sqs create-queue --queue-name local.fifo --attributes "{\"FifoQueue\":\"true\"}"

aws --endpoint-url http://localstack:4566 sqs create-queue --queue-name updater-queue

aws --endpoint-url http://localstack:4566 s3api create-bucket --bucket pathmind.local

aws --endpoint-url http://localstack:4566  sns create-topic --name updater-local-topic
aws --endpoint-url http://localstack:4566  sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:updater-local-topic --protocol sqs --notification-endpoint http://localstack:4566/000000000000/updater-queue  --attributes "{\"RawMessageDelivery\":\"true\"}"

cd /pathmind-database || exit
mvn liquibase:update

cd /fixture-files || exit
for dir in */ # list directories in the form "/fixture-files"
do
    dir=${dir%*/}      # remove the trailing "/"
    echo "${dir}"
    aws --endpoint-url http://localstack:4566 s3api create-bucket --bucket "${dir}"
    aws --endpoint-url http://localstack:4566 s3 sync "${dir}" "s3://${dir}/"
done


#sleep 1000000000