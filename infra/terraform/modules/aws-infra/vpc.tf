# Provision NAT EIPs outside of the VPC so they are not managed by the VPC module
resource "aws_eip" "nat" {
  count = var.single_nat_gateway ? 1 : length(var.private_subnets)
  vpc   = true
  tags = {
    Name        = "${var.environment} NAT Gateway EIP ${count.index}"
    Environment = var.environment
    Terraform   = "true"
  }
}

# Provision VPC
module "vpc" {
  source = "../aws-vpc"

  name = var.environment
  cidr = var.vpc_cidr

  azs             = var.vpc_azs
  private_subnets = var.private_subnets
  public_subnets  = var.public_subnets
  private_subnet_tags = var.private_subnet_tags
  public_subnet_tags  = var.public_subnet_tags


  enable_dhcp_options              = var.enable_dhcp_options
  dhcp_options_domain_name_servers = var.dhcp_options_domain_name_servers

  enable_dns_hostnames = true
  enable_nat_gateway   = true
  single_nat_gateway   = var.single_nat_gateway
  reuse_nat_ips        = true               # <= Skip creation of EIPs for the NAT Gateways
  external_nat_ip_ids  = aws_eip.nat.*.id # <= IPs specified here as input to the module
  allow_public_ingress = var.allow_public_ingress
  trusted_networks     = var.trusted_networks
  enable_s3_endpoint   = true

  tags = {
    Environment = var.environment
    Terraform   = "true"
  }
}

