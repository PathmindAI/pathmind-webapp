terraform {
  backend "s3" {
    bucket = "test-terraform-state.pathmind.com"
    key    = "cluster.tfstate"
    region = "us-east-1"
  }
}

