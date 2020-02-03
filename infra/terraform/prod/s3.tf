#Bucket to hold files used by the training
resource "aws_s3_bucket" "training-static-files" {
  bucket        = "${var.environment}-training-static-files.pathmind.com"
  acl           = "private"
  force_destroy = false

  versioning {
    enabled = false
  }

  tags = {
    Name = "pathmind"
  }
}

resource "aws_s3_bucket" "training-dymanic-files" {
  bucket        = "${var.environment}-training-dynamic-files.pathmind.com"
  acl           = "private"
  force_destroy = false

  versioning {
    enabled = false
  }

  tags = {
    Name = "pathmind"
  }
}
