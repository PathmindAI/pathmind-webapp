terraform {
  backend "s3" {
    bucket = "dev-terraform-state.pathmind.com"
    key    = "cluster.tfstate"
    region = "us-east-1"
  }
}

