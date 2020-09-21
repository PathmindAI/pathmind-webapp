export DB_URL='jdbc:postgresql://localhost/pathmind?user=pathmind&password=pathmind';
export AWS_DEFAULT_REGION=us-east-1;
export AWS_ACCESS_KEY_ID=X;
export AWS_SECRET_ACCESS_KEY=Y;
export S3_BUCKET=pathmind.local; #that corresponds to the ./data/pathmind.local folder
export SQS_URL=http://localhost:4566/000000000000/local.fifo;
export JOB_MOCK_CYCLE=10;
export DEBUG_ACCELERATE=true;
export SQS_UPDATER_URL=http://localhost:4566/000000000000/updater-queue;
export AWS_ENDPOINT_URL=http://localhost:4566;
export SNS_UPDATER_SQS_FILTER_ATTR=localstack;