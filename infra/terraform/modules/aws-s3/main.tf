################
# Example bucket
################
# resource "aws_s3_bucket" "example-bucket" {
#   # random string since buckets are global
#   bucket = "example-bucket-iejwef833161234"
#   acl    = "bucket-owner-full-control"
#   versioning {
#     enabled = false
#   }
# server_side_encryption_configuration {
#   rule {
#     apply_server_side_encryption_by_default {
#       sse_algorithm = "AES256"
#     }
#   }
# }
#   tags {
#     Name = "Example Bucket"
#     Terraform = "true"
#   }
#   region = "${var.region}"
#
#   # TF metadata
#   lifecycle {
#     prevent_destroy = true
#   }
# }
