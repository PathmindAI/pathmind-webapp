module "aws-security-groups" {
  source = "../aws-security-groups"

  vpc_id         = module.vpc.vpc_id
  vpc_cidr_block = module.vpc.vpc_cidr_block
  environment    = var.environment

  tags = {
    Environment = var.environment
    Terraform   = "true"
  }

  create_external_ssh_sg = var.create_external_ssh_sg
  create_external_web_sg = var.create_external_web_sg
  create_internal_ssh_sg = var.create_internal_ssh_sg
  create_internal_web_sg = var.create_internal_web_sg
  create_vpn_sg          = var.create_vpn_sg
  create_efs_sg          = var.create_efs_sg
  trusted_networks       = var.trusted_networks
}

