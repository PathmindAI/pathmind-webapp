terraform {
  backend "s3" {
    bucket = "prod-terraform-state.pathmind.com"
    key    = "peering.tfstate"
    region = "us-east-1"
  }
}

