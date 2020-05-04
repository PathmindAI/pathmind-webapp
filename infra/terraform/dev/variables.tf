variable "region" { default = "us-east-1"}
variable "vpc_id" { }
variable "awsaccesskey" {}
variable "awssecretaccesskey" {}
variable "domain_name" {}
variable "subdomain" {}
variable "appdomain" {}
variable "DB_URL_DEV" {}
variable "DB_URL_CLI_DEV" {}
variable "DB_URL_TEST" {}
variable "DB_URL_CLI_TEST" {}
variable "DB_URL_PROD" {}
variable "SEGMENT_WEBSITE_KEY" {}
variable "SEGMENT_SERVER_KEY" {}
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
variable "node_count" {  }
variable "node_size" {  }
variable "master_size" {  }
variable "cidr_block" {  }
variable "apipassword" {  }
variable "jenkinspassword" {  }
variable "pgadminpassword" {  }
variable "githubuser" {  }
variable "githubsecret" {  }
