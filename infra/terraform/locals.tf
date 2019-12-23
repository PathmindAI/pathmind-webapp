locals {
  region                 = "us-east-1"
  master_zones           = "us-east-1a"
  node_zones             = "us-east-1a,us-east-1b"
  vpc_name               = "pathmind-vpc"
  domain_name            = "danieljj.com"
  cluster_name           = "pathmind.k8s.local"
  kops_bucket            = "pathmind-kops-state"
  trigger_function_name  = "triggerTraining"
  environment            = "production"

  tags = {
    environment = "${local.environment}"
    terraform   = true
  }
}
