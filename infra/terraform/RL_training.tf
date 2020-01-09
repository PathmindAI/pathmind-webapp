#Bucket to hold files used by the training
resource "aws_s3_bucket" "training-static-files" {
  bucket        = "${var.environment}-training-static-files.pathmind.com"
  acl           = "private"
  force_destroy = true

  versioning {
    enabled = true
  }

  tags = {
    Name = "pathmind"
  }
}

resource "aws_s3_bucket" "training-dymanic-files" {
  bucket        = "${var.environment}-training-dynamic-files.pathmind.com"
  acl           = "private"
  force_destroy = true

  versioning {
    enabled = true
  }

  tags = {
    Name = "pathmind"
  }
}



#Create SQS
resource "aws_sqs_queue" "training_queue" {
  name                      = "${var.environment}-training-queue.fifo"
  fifo_queue                  = true
  content_based_deduplication = true

  tags = {
    Environment = "pathmind"
  }
}

