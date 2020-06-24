variable "create_internal_web_sg" {
  default = true
}

resource "aws_security_group" "internal_web" {
  description = "Allow internal web traffic"
  name        = "${var.environment} internal_web"
  count       = var.create_internal_web_sg ? 1 : 0
  vpc_id      = var.vpc_id
  tags = merge(
    {
      "Name" = "${var.environment} internal_web"
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

resource "aws_security_group_rule" "trusted_networks_to_http" {
  count             = var.create_internal_web_sg ? 1 : 0
  type              = "ingress"
  from_port         = 80
  to_port           = 80
  protocol          = "tcp"
  cidr_blocks       = var.trusted_networks
  security_group_id = element(concat(aws_security_group.internal_web.*.id, [""]), 0)
}

resource "aws_security_group_rule" "trusted_networks_to_https" {
  count             = var.create_internal_web_sg ? 1 : 0
  type              = "ingress"
  from_port         = 443
  to_port           = 443
  protocol          = "tcp"
  cidr_blocks       = var.trusted_networks
  security_group_id = element(concat(aws_security_group.internal_web.*.id, [""]), 0)
}

resource "aws_security_group_rule" "trusted_networks_to_8000" {
  count             = var.create_internal_web_sg ? 1 : 0
  type              = "ingress"
  from_port         = 8000
  to_port           = 8000
  protocol          = "tcp"
  cidr_blocks       = var.trusted_networks
  security_group_id = element(concat(aws_security_group.internal_web.*.id, [""]), 0)
}

resource "aws_security_group_rule" "trusted_networks_to_8080" {
  count             = var.create_internal_web_sg ? 1 : 0
  type              = "ingress"
  from_port         = 8080
  to_port           = 8080
  protocol          = "tcp"
  cidr_blocks       = var.trusted_networks
  security_group_id = element(concat(aws_security_group.internal_web.*.id, [""]), 0)
}

output "internal_web_sg_id" {
  description = "Internal Web Security Group ID"
  value       = element(concat(aws_security_group.internal_web.*.id, [""]), 0)
}

