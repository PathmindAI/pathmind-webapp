#Create SQS
resource "aws_sqs_queue" "training_queue" {
  name                      = "${var.environment}eks-training-queue.fifo"
  fifo_queue                  = true
  content_based_deduplication = true

  tags = {
    Environment = "pathmind"
  }
}

resource "aws_sqs_queue" "updater_queue" {
  name                      = "${var.environment}eks-updater-queue"
  message_retention_seconds   = 60
  policy = <<EOF
{

  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sqs:*",
      "Effect": "Allow",
      "Condition": {"ArnEquals": {"aws:SourceArn": "${aws_sns_topic.updater_topic.arn}"}},
      "Principal": "*"
    }
  ]
}
EOF


  tags = {
    Environment = "pathmind"
  }
}

