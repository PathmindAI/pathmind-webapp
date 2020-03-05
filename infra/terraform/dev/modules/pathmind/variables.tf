variable "awsaccesskey" {}
variable "awssecretaccesskey" {}
variable "DB_URL" {}
variable "DB_URL_CLI" {}
variable "SEGMENT_WEBSITE_KEY" {}
variable "SEGMENT_SERVER_KEY" {}
variable "environment" { }
variable "node_subnet_ids" {
  type    = "list"
}
variable "vpc_id" {}
variable "db_allocated_storage" {}
variable "db_instance_class" {}
variable "multi_az" {}
variable "database_name" {}
variable "database_username" {}
variable "database_password" {}
variable "apipassword" {}
