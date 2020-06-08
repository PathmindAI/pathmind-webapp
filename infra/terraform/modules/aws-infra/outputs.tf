# VPC outputs
output "vpc_id" {
  value = module.vpc.vpc_id
}

output "vpc_cidr_block" {
  value = module.vpc.vpc_cidr_block
}

output "private_subnets" {
  value = module.vpc.private_subnets
}

output "public_subnets" {
  value = module.vpc.public_subnets
}

output "private_route_table_ids" {
  value = module.vpc.private_route_table_ids
}

output "private_route_table_count" {
  value = module.vpc.private_route_table_count
}

output "public_route_table_ids" {
  value = module.vpc.public_route_table_ids
}

output "public_route_table_count" {
  value = module.vpc.public_route_table_count
}

# Security groups outputs
output "external_ssh_sg_id" {
  value = module.aws-security-groups.external_ssh_sg_id
}

output "external_web_sg_id" {
  value = module.aws-security-groups.external_web_sg_id
}

output "internal_ssh_sg_id" {
  value = module.aws-security-groups.internal_ssh_sg_id
}

output "internal_web_sg_id" {
  value = module.aws-security-groups.internal_web_sg_id
}

#output "internal_ecs_sg_id" {
#  value = module.aws-security-groups.internal_ecs_sg_id
#}

output "vpn_sg_id" {
  value = module.aws-security-groups.vpn_sg_id
}

output "efs_sg_id" {
  value = module.aws-security-groups.efs_sg_id
}

output "kms_key_alias" {
  value = aws_kms_alias.env_key_alias.name
}

output "kms_key_arn" {
  value = aws_kms_key.env_key.arn
}

