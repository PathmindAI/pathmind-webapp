variable "region" { default = "us-east-1"}
variable "awsaccesskey" {}
variable "awssecretaccesskey" {}
variable "DB_URL" {}
variable "DB_URL_CLI" {}
variable "SEGMENT_KEY" {}
variable "environment" { }
variable "db_allocated_storage" { }

variable "db_instance_class" { }
variable "multi_az" { }
variable "database_name" { }
variable "database_username" { }
variable "database_password" {}

variable "cluster_name" {  }
variable "kops_bucket" {  }
variable "db_s3_bucket" { }
variable "db_s3_file" { }
variable "master_zones" {  }
variable "node_zones" {  }

