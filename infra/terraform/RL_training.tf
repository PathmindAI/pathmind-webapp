#Bucket to hold files used by the training
resource "aws_s3_bucket" "training-files" {
  bucket        = "training-files.${local.domain_name}"
  acl           = "private"
  force_destroy = true

  versioning {
    enabled = true
  }

  tags = {
    Name = "pathmind"
  }
}

#Create SQS
resource "aws_sqs_queue" "training_queue" {
  name                      = "training-queue.fifo"
  fifo_queue                  = true
  content_based_deduplication = true

  tags = {
    Environment = "pathmind"
  }
}


#Create Role for training
resource "aws_iam_role" "training_role" {
  name = "training-role"
  path = "/service-role/"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF

}

resource "aws_lambda_function" "trigger_training_function" {
  role             = aws_iam_role.training_role.arn
  handler          = "triggerTraining.lambda_handler"
  runtime          = "python3.6"
  filename         = "files/triggerTraining.zip"
  function_name    = local.trigger_function_name
  source_code_hash = base64sha256(filebase64("files/triggerTraining.zip"))
  tags ={
    Name = "pathmind"
  }
}

resource "aws_iam_policy" "lambda_exec_policy" {
  name        = "lambda_exec_policy"
  path        = "/"
  description = "Lambda execution policy"

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "logs:CreateLogGroup",
            "Resource": "arn:aws:logs:us-east-1:839270835622:*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "logs:CreateLogStream",
                "logs:PutLogEvents"
            ],
            "Resource": [
                "arn:aws:logs:us-east-1:839270835622:log-group:/aws/lambda/${local.trigger_function_name}:*"
            ]
        }
    ]
}
EOF
}

resource "aws_iam_policy" "lambda_sqs_policy" {
  name        = "sqs_policy"
  path        = "/"
  description = "Policy to read SQS from lambda"

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "sqs:DeleteMessage",
                "sqs:GetQueueAttributes",
                "sqs:ReceiveMessage"
            ],
            "Resource": "arn:aws:sqs:*"
        }
    ]
}
EOF
}

#Assign Policies to training
resource "aws_iam_role_policy_attachment" "ec2_policy-attach" {
  role       = aws_iam_role.training_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2FullAccess"
}

resource "aws_iam_role_policy_attachment" "lambda_sqs_policy-attach" {
  role       = aws_iam_role.training_role.name
  policy_arn = aws_iam_policy.lambda_sqs_policy.arn
}

resource "aws_iam_role_policy_attachment" "TriggerRoleSQSPolicy-attach" {
  role       = aws_iam_role.training_role.name
  policy_arn = aws_iam_policy.lambda_exec_policy.arn
}


resource "aws_lambda_event_source_mapping" "sqs_trigger_function" {
  event_source_arn = aws_sqs_queue.training_queue.arn
  function_name    = aws_lambda_function.trigger_training_function.arn
}
