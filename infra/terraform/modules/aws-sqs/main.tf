resource "aws_sqs_queue" "main" {
  name                        = var.name
  fifo_queue                  = true
  content_based_deduplication = true
}
