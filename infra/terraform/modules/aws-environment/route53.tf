# data source for Route53 Hosted Zone
# used to create Route53 records for various resources

data "aws_route53_zone" "typography_com" {
  name = "typography.com."
}

# Create zone based on env. Uncomment to create this

# resource "aws_route53_zone" "private" {
#   name = "${var.environment}.typography.com."

#   vpc {
#     vpc_id = "${module.infra.vpc_id}"
#   }

#   tags {
#     Name        = "${var.environment} internal-hosted-zone"
#     Environment = "${var.environment}"
#     Terraform   = "true"
#   }
# }

// resource "aws_route53_zone" "infra" {
//   name = "${var.environment}.typography.com."

//   tags = {
//     Name        = "${var.environment} public zone"
//     Environment = var.environment
//     Terraform   = "true"
//   }
// }

