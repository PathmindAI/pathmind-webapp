provider "aws" {
  version = ">= 2.11"
  region  = local.region
}

provider "local" {
  version = "~> 1.2"
}

provider "null" {
  version = "~> 2.1"
}

provider "template" {
  version = "~> 2.1"
}

provider "external" {}

provider "helm" {
  kubernetes {
    config_path = "/home/ec2-user/.kube/config"
  }
}

