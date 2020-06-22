variable "create_vpn_sg" {
  default = false
}

resource "aws_security_group" "vpn" {
  description = "Allow vpn access"
  name        = "${var.environment} vpn"
  count       = var.create_vpn_sg ? 1 : 0
  vpc_id      = var.vpc_id
  tags = merge(
    {
      "Name" = "${var.environment} vpn"
    },
    var.tags,
  )

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group_rule" "vpn" {
  count             = var.create_vpn_sg ? 1 : 0
  type              = "ingress"
  from_port         = 1194
  to_port           = 1194
  protocol          = "udp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = element(concat(aws_security_group.vpn.*.id, [""]), 0)
}

output "vpn_sg_id" {
  description = "VPN Security Group ID"
  value       = element(concat(aws_security_group.vpn.*.id, [""]), 0)
}

