

resource "aws_network_acl_rule" "allow_public_ingress_vpn" {
  count = var.vpn_enabled ? 1 : 0
  network_acl_id = element(tolist(data.aws_network_acls.public.ids), 0)
  rule_number    = 194
  egress         = false
  protocol       = "udp"
  rule_action    = "allow"
  cidr_block     = "0.0.0.0/0"
  from_port      = 1194
  to_port        = 1194
}

resource "aws_network_acl_rule" "allow_public_ingress_ssh" {
  count = var.ssh_enabled ? 1 : 0
  network_acl_id = element(tolist(data.aws_network_acls.public.ids), 0)
  rule_number    = 122
  egress         = false
  protocol       = "tcp"
  rule_action    = "allow"
  cidr_block     = "0.0.0.0/0"
  from_port      = 22
  to_port        = 22
}

