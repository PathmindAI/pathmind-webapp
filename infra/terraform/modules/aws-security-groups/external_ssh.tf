variable "create_external_ssh_sg" {
  default = true
}

resource "aws_security_group" "external_ssh" {
  description = "Allow external SSH traffic"
  name        = "${var.environment} external ssh"
  count       = var.create_external_ssh_sg ? 1 : 0
  vpc_id      = var.vpc_id
  tags = merge(
    {
      "Name" = "${var.environment} external ssh"
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

resource "aws_security_group_rule" "external_ssh" {
  count             = var.create_external_ssh_sg ? 1 : 0
  type              = "ingress"
  from_port         = 22
  to_port           = 22
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = element(concat(aws_security_group.external_ssh.*.id, [""]), 0)
}

output "external_ssh_sg_id" {
  description = "External SSH Security Group ID"
  value       = element(concat(aws_security_group.external_ssh.*.id, [""]), 0)
}

