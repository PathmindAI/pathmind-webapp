region           = "us-east-1"
name             = "AdminUsersDev"
hash_key         = "AdminUser"
billing_mode     = "PROVISIONED"
read_capacity    = 5
write_capacity   = 5

attributes = [{
  name = "AdminUser"
  type = "S"
}]

