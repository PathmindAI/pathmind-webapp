resource "aws_sns_topic" "updater_topic" {
  name = "${var.environment}-updater-topic"
}

resource "aws_sns_topic_subscription" "sns_sqs_target" {
  topic_arn = "${aws_sns_topic.updater_topic.arn}"
  protocol  = "sqs"
  endpoint  = "${aws_sqs_queue.updater_queue.arn}"
  raw_message_delivery = true
}

resource "aws_sns_topic_subscription" "sns_sqs_target_alexander" {
  topic_arn = "${aws_sns_topic.updater_topic.arn}"
  protocol  = "sqs"
  endpoint  = "${aws_sqs_queue.updater_queue_alexander.arn}"
  raw_message_delivery = true
}

resource "aws_sns_topic_subscription" "sns_sqs_target_brett" {
  topic_arn = "${aws_sns_topic.updater_topic.arn}"
  protocol  = "sqs"
  endpoint  = "${aws_sqs_queue.updater_queue_brett.arn}"
  raw_message_delivery = true
}

resource "aws_sns_topic_subscription" "sns_sqs_target_bruno" {
  topic_arn = "${aws_sns_topic.updater_topic.arn}"
  protocol  = "sqs"
  endpoint  = "${aws_sqs_queue.updater_queue_bruno.arn}"
  raw_message_delivery = true
}

resource "aws_sns_topic_subscription" "sns_sqs_target_daeh" {
  topic_arn = "${aws_sns_topic.updater_topic.arn}"
  protocol  = "sqs"
  endpoint  = "${aws_sqs_queue.updater_queue_daeh.arn}"
  raw_message_delivery = true
}

resource "aws_sns_topic_subscription" "sns_sqs_target_edward" {
  topic_arn = "${aws_sns_topic.updater_topic.arn}"
  protocol  = "sqs"
  endpoint  = "${aws_sqs_queue.updater_queue_edward.arn}"
  raw_message_delivery = true
}

resource "aws_sns_topic_subscription" "sns_sqs_target_evgeniy" {
  topic_arn = "${aws_sns_topic.updater_topic.arn}"
  protocol  = "sqs"
  endpoint  = "${aws_sqs_queue.updater_queue_evgeniy.arn}"
  raw_message_delivery = true
}

resource "aws_sns_topic_subscription" "sns_sqs_target_fionna" {
  topic_arn = "${aws_sns_topic.updater_topic.arn}"
  protocol  = "sqs"
  endpoint  = "${aws_sqs_queue.updater_queue_fionna.arn}"
  raw_message_delivery = true
}

resource "aws_sns_topic_subscription" "sns_sqs_target_henri" {
  topic_arn = "${aws_sns_topic.updater_topic.arn}"
  protocol  = "sqs"
  endpoint  = "${aws_sqs_queue.updater_queue_henri.arn}"
  raw_message_delivery = true
}

resource "aws_sns_topic_subscription" "sns_sqs_target_onur" {
  topic_arn = "${aws_sns_topic.updater_topic.arn}"
  protocol  = "sqs"
  endpoint  = "${aws_sqs_queue.updater_queue_onur.arn}"
  raw_message_delivery = true
}

resource "aws_sns_topic_subscription" "sns_sqs_target_slin" {
  topic_arn = "${aws_sns_topic.updater_topic.arn}"
  protocol  = "sqs"
  endpoint  = "${aws_sqs_queue.updater_queue_slin.arn}"
  raw_message_delivery = true
}

resource "aws_sns_topic_subscription" "sns_sqs_target_steph" {
  topic_arn = "${aws_sns_topic.updater_topic.arn}"
  protocol  = "sqs"
  endpoint  = "${aws_sqs_queue.updater_queue_steph.arn}"
  raw_message_delivery = true
}

