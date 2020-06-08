resource "aws_security_group" "rds-sg" {
  name   = "${var.environment}-${var.identifier}-rds-rds"
  vpc_id = data.aws_vpc.selected.id
}

resource "aws_security_group_rule" "rds_ingress" {
  type              = "ingress"
  from_port         = var.port
  to_port           = var.port
  protocol          = "tcp"
  cidr_blocks       = concat(var.allowed_cidrs, ["${data.aws_vpc.selected.cidr_block}"])
  security_group_id = aws_security_group.rds-sg.id
}

resource "aws_security_group_rule" "rds_egress" {
  type              = "egress"
  from_port         = var.port
  to_port           = var.port
  protocol          = "tcp"
  cidr_blocks       = concat(var.allowed_cidrs, ["${data.aws_vpc.selected.cidr_block}"])
  security_group_id = aws_security_group.rds-sg.id
}