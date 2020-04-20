resource "aws_dynamodb_table" "admin_users" {
  name             = "AdminUsersProd"
  hash_key         = "AdminUser"
  billing_mode     = "PROVISIONED"
  read_capacity    = 5
  write_capacity   = 5

  attribute {
    name = "AdminUser"
    type = "S"
  }
}
