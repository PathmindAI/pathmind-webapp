provider "aws" {
  version = ">= 2.11"
  region  = "${var.region}"
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

