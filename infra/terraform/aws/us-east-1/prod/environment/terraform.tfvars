region="us-east-1"
environment="production"
tf_state_bucket_name="pathmind-terraform-state"

vpc_cidr="10.10.0.0/16"
# list of AZs to create subnets in
vpc_azs=["us-east-1a", "us-east-1b"]
# CIDRs of subnets
public_subnets=["10.10.1.0/24", "10.10.2.0/24"]
private_subnets=["10.10.10.0/24", "10.10.12.0/24"]
# If true, one NAT GW will be used for all private IPs (not recommended in prod)
single_nat_gateway="true"

# CIDRs to allow egress/ingress. Include this vpc and other CIDR blocks (especially admin VPC) after which will get added to ACL/SGs
# This includes CIDRs for VPN/Office networks
trusted_networks=[
  "10.1.0.0/16",    # Admin
  "10.10.0.0/16",    # Production
  "10.20.0.0/16"    # Staging
]

# If true, will create acl rules for these
ssh_enabled = false
vpn_enabled = false
# if true, will allow 443/80 from 0.0.0.0/0
allow_public_ingress="true"

# set custom domain servers
enable_dhcp_options="false"

# SSH keypair name used to launch EC2 instances
key_pair_name="pathmind-aws"

# ECR/ECS variables
create_ecr = false
desired_capacity = 0

# Route53 private hosted zone to create
create_hosted_zone=false

# Subnet tags
public_subnet_tags = {
  "kubernetes.io/cluster/eks-prod" = "shared",
  "kubernetes.io/role/elb" = "1"
}

private_subnet_tags = {
  "kubernetes.io/cluster/eks-prod" = "shared",
  "kubernetes.io/role/internal-elb" = "1"
}
