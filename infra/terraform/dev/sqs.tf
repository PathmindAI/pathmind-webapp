#Create SQS
resource "aws_sqs_queue" "training_queue" {
  name                      = "${var.environment}-training-queue.fifo"
  fifo_queue                  = true
  content_based_deduplication = true

  tags = {
    Environment = "pathmind"
  }
}

resource "aws_sqs_queue" "test_updater_queue" {
  name                      = "test-updater-queue.fifo"
  fifo_queue                  = true

  tags = {
    Environment = "pathmind"
  }
}

