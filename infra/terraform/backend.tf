terraform {
  backend "s3" {
    bucket = "state.pathmind.com"
    key    = "terraform/cluster.tfstate"
    region = "us-east-1"
  }
}
