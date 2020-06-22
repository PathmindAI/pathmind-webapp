variable "create_external_web_sg" {
  default = true
}

resource "aws_security_group" "external_web" {
  description = "Allow external web traffic"
  name        = "${var.environment} external_web"
  count       = var.create_external_web_sg ? 1 : 0
  vpc_id      = var.vpc_id
  tags = merge(
    {
      "Name" = "${var.environment} external_web"
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

resource "aws_security_group_rule" "external_web_http" {
  count             = var.create_external_web_sg ? 1 : 0
  type              = "ingress"
  from_port         = 80
  to_port           = 80
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = element(concat(aws_security_group.external_web.*.id, [""]), 0)
}

resource "aws_security_group_rule" "external_web_https" {
  count             = var.create_external_web_sg ? 1 : 0
  type              = "ingress"
  from_port         = 443
  to_port           = 443
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = element(concat(aws_security_group.external_web.*.id, [""]), 0)
}

output "external_web_sg_id" {
  description = "External Web Security Group ID"
  value       = element(concat(aws_security_group.external_web.*.id, [""]), 0)
}

