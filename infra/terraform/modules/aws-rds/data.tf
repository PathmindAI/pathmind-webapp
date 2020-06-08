
data "aws_ssm_parameter" "db_password" {
  depends_on = [null_resource.db_password]
  name       = local.value
}

# Get subnets dynamically 
data "aws_vpc" "selected" {
  filter {
    name   = "tag:Name"
    values = [var.environment]
  }
}

data "aws_subnet_ids" "selected" {
  vpc_id = data.aws_vpc.selected.id

  filter {
    name   = "tag:Name"
    values = ["${var.environment}-private-*"]
  }
}

#data "aws_route53_zone" "selected" {
#  name = "typography.com."
#}
