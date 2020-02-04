terraform {
  backend "s3" {
    bucket = "prod-terraform-state.pathmind.com"
    key    = "cluster.tfstate"
    region = "us-east-1"
  }
}

