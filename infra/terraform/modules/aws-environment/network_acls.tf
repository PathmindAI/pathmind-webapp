data "aws_network_acls" "public" {
  vpc_id = module.infra.vpc_id
  filter {
    name = "association.subnet-id"
    values = [element(module.infra.public_subnets, 0)]
  }
}

# resource "aws_network_acl_rule" "allow_public_ingress_ssh" {
#   network_acl_id = "${element(data.aws_network_acls.public.ids, 0)}"
#   rule_number = 122
#   egress = false
#   protocol = "tcp"
#   rule_action = "allow"
#   cidr_block = "0.0.0.0/0"
#   from_port = 22
#   to_port = 22
# }
