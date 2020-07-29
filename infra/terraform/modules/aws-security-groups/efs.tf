variable "create_efs_sg" {
  default = false
}

resource "aws_security_group" "efs" {
  description = "Allow efs access"
  name        = "${var.environment} efs"
  count       = var.create_efs_sg ? 1 : 0
  vpc_id      = var.vpc_id
  tags = merge(
    {
      "Name" = "${var.environment} efs"
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

resource "aws_security_group_rule" "efs" {
  count             = var.create_efs_sg ? 1 : 0
  type              = "ingress"
  from_port         = 2049
  to_port           = 2049
  protocol          = "tcp"
  cidr_blocks       = var.trusted_networks
  security_group_id = element(concat(aws_security_group.efs.*.id, [""]), 0)
}

output "efs_sg_id" {
  description = "EFS Security Group ID"
  value       = element(concat(aws_security_group.efs.*.id, [""]), 0)
}

