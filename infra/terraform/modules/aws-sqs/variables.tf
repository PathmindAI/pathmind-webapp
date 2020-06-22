variable "delay_seconds" {
  default = "0"
}

variable "kms_data_key_reuse_period_seconds" {
  description = "The length of time, in seconds, for which Amazon SQS can reuse a data key to encrypt or decrypt messages before calling AWS KMS again. An integer representing seconds, between 60 seconds (1 minute) and 86,400 seconds (24 hours)"
  default     = "300"
}

variable "kms_master_key_id" {
  description = "The ID of an AWS-managed customer master key (CMK) for Amazon SQS or a custom CMK"
}

variable "max_message_size" {
  default = "262144"
}

variable "message_retention_seconds" {
  default = "345600"
}

variable "name" {
}

variable "policy" {
  default = "{}"
}

variable "receive_wait_time_seconds" {
  default = "0"
}

variable "redrive_policy" {
  default = "{}"
}

variable "tags" {
  description = "A mapping of tags to assign to all resources"
  type        = map(string)
  default     = {}
}

variable "visibility_timeout_seconds" {
  default = "30"
}

