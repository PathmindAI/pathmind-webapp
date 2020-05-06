resource "aws_sns_topic" "updater_topic" {
  name = "${var.environment}-updater-topic"
}


resource "aws_sns_topic_subscription" "sns_sqs_target" {
  topic_arn = "${aws_sns_topic.updater_topic.arn}"
  protocol  = "sqs"
  endpoint  = "${aws_sqs_queue.updater_queue.arn}"
  raw_message_delivery = true
}

