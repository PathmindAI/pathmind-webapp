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


resource "aws_sqs_queue" "updater_queue_alexander" {
  name                      = "alexander-updater-queue-${var.environment}"
  message_retention_seconds   = 60

  tags = {
    Environment = "pathmind"
  }
}

resource "aws_sqs_queue" "updater_queue_ali" {
  name                      = "ali-updater-queue-${var.environment}"
  message_retention_seconds   = 60

  tags = {
    Environment = "pathmind"
  }
}

resource "aws_sqs_queue" "updater_queue_brett" {
  name                      = "brett-updater-queue-${var.environment}"
  message_retention_seconds   = 60

  tags = {
    Environment = "pathmind"
  }
}

resource "aws_sqs_queue" "updater_queue_bruno" {
  name                      = "bruno-updater-queue-${var.environment}"
  message_retention_seconds   = 60

  tags = {
    Environment = "pathmind"
  }
}

resource "aws_sqs_queue" "updater_queue_daeh" {
  name                      = "daeh-updater-queue-${var.environment}"
  message_retention_seconds   = 60

  tags = {
    Environment = "pathmind"
  }
}

resource "aws_sqs_queue" "updater_queue_edward" {
  name                      = "edward-updater-queue-${var.environment}"
  message_retention_seconds   = 60

  tags = {
    Environment = "pathmind"
  }
}

resource "aws_sqs_queue" "updater_queue_evgeniy" {
  name                      = "evgeniy-updater-queue-${var.environment}"
  message_retention_seconds   = 60

  tags = {
    Environment = "pathmind"
  }
}

resource "aws_sqs_queue" "updater_queue_fionna" {
  name                      = "fionna-updater-queue-${var.environment}"
  message_retention_seconds   = 60

  tags = {
    Environment = "pathmind"
  }
}

resource "aws_sqs_queue" "updater_queue_henri" {
  name                      = "henri-updater-queue-${var.environment}"
  message_retention_seconds   = 60

  tags = {
    Environment = "pathmind"
  }
}

resource "aws_sqs_queue" "updater_queue_onur" {
  name                      = "onur-updater-queue-${var.environment}"
  message_retention_seconds   = 60

  tags = {
    Environment = "pathmind"
  }
}

resource "aws_sqs_queue" "updater_queue_slin" {
  name                      = "slin-updater-queue-${var.environment}"
  message_retention_seconds   = 60

  tags = {
    Environment = "pathmind"
  }
}

resource "aws_sqs_queue" "updater_queue_steph" {
  name                      = "steph-updater-queue-${var.environment}"
  message_retention_seconds   = 60

  tags = {
    Environment = "pathmind"
  }
}

