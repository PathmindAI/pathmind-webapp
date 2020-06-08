# VPC outputs
output "vpc_id_admin" {
  value = module.infra.vpc_id
}

output "vpc_cidr_block" {
  value = module.infra.vpc_cidr_block
}

output "private_subnets" {
  value = module.infra.private_subnets
}

output "public_subnets" {
  value = module.infra.public_subnets
}

output "private_route_table_ids" {
  value = module.infra.private_route_table_ids
}

output "public_route_table_ids" {
  value = module.infra.public_route_table_ids
}

# Security groups outputs
output "external_ssh_sg_id" {
  value = module.infra.external_ssh_sg_id
}

output "external_web_sg_id" {
  value = module.infra.external_web_sg_id
}

output "internal_ssh_sg_id" {
  value = module.infra.internal_ssh_sg_id
}

output "internal_web_sg_id" {
  value = module.infra.internal_web_sg_id
}

# KMS outputs
output "kms_key_alias" {
  value = module.infra.kms_key_alias
}

output "kms_key_arn" {
  value = module.infra.kms_key_arn
}

# ECR/ECS outputs

#output "ecr_repos" {
#  value = aws_ecr_repository.ecr_repo.*.repository_url
#}

#output "ecs_cluster_name" {
#  value = module.infra.ecs_cluster_name
#}

#output "ecs_cluster_arn" {
#  value = module.infra.ecs_cluster_arn
#}

