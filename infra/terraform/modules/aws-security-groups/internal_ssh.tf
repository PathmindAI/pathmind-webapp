variable "create_internal_ssh_sg" {
  default = true
}

resource "aws_security_group" "internal_ssh" {
  description = "Allow internal SSH traffic"
  name        = "${var.environment} internal ssh"
  count       = var.create_internal_ssh_sg ? 1 : 0
  vpc_id      = var.vpc_id
  tags = merge(
    {
      "Name" = "${var.environment} internal ssh"
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

resource "aws_security_group_rule" "internal_ssh" {
  count             = var.create_internal_ssh_sg ? 1 : 0
  type              = "ingress"
  from_port         = 22
  to_port           = 22
  protocol          = "tcp"
  cidr_blocks       = var.trusted_networks
  security_group_id = element(concat(aws_security_group.internal_ssh.*.id, [""]), 0)
}

output "internal_ssh_sg_id" {
  description = "Internal SSH Security Group ID"
  value       = element(concat(aws_security_group.internal_ssh.*.id, [""]), 0)
}

