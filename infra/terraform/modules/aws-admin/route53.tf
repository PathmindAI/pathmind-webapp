# data source for Route53 Hosted Zone
# used to create Route53 records for various resources
# data "aws_route53_zone" "typography_com" {
#   name = "typography.com."
# }

# Create internal zone
resource "aws_route53_zone" "private" {
  count = var.create_hosted_zone ? 1 : 0
  name = "${var.hosted_zone_name}."

  vpc {
    vpc_id = module.infra.vpc_id
  }

  tags = {
    Name        = "${var.environment} internal-hosted-zone"
    Environment = var.environment
    Terraform   = "true"
  }
}

