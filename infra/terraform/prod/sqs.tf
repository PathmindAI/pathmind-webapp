#Create SQS
resource "aws_sqs_queue" "training_queue" {
  name                      = "${var.environment}-training-queue.fifo"
  fifo_queue                  = true
  content_based_deduplication = true

  tags = {
    Environment = "pathmind"
  }
}

resource "aws_sqs_queue" "updater_queue" {
  name                      = "${var.environment}-updater-queue"
  message_retention_seconds   = 60

  tags = {
    Environment = "pathmind"
  }
}
