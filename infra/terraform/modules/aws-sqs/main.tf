resource "aws_sqs_queue" "main" {
  name                        = var.name
  fifo_queue                  = var.fifo_queue
  message_retention_seconds   = var.message_retention_seconds
  content_based_deduplication = var.content_based_deduplication
}
