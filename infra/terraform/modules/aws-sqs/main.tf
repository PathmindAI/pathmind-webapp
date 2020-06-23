resource "aws_sqs_queue" "main" {
  name                              = var.name
  visibility_timeout_seconds        = var.visibility_timeout_seconds
  delay_seconds                     = var.delay_seconds
  max_message_size                  = var.max_message_size
  message_retention_seconds         = var.message_retention_seconds
  receive_wait_time_seconds         = var.receive_wait_time_seconds
  policy                            = var.policy
  redrive_policy                    = var.redrive_policy
  kms_master_key_id                 = var.kms_master_key_id
  kms_data_key_reuse_period_seconds = var.kms_data_key_reuse_period_seconds
  tags                              = var.tags
}

