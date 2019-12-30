##############################################################
# Data sources to get VPC, subnets and security group details
##############################################################
#Get k8s vpc id
#data "external" "vpc_id" {
#  program    = ["bash", "./scripts/get_vpc_id.sh", "${local.cluster_name}"]
#  depends_on = [null_resource.k8s]
#}

data "aws_vpc" "selected" {
  filter {
    name   = "tag:Name"
    values = [local.cluster_name]
  }
  depends_on = [null_resource.k8s]
}

data "aws_subnet_ids" "all" {
  vpc_id = data.aws_vpc.selected.id
}

resource "aws_security_group" "pathminddb" {
  name = "pathminddb"

  description = "RDS postgres servers (terraform-managed)"
  vpc_id = data.aws_vpc.selected.id

  # Only postgres in
  ingress {
    from_port = 5432
    to_port = 5432
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Allow all outbound traffic.
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  depends_on = [null_resource.k8s]
}


#####
# DB
#####
module "db" {
  source = "terraform-aws-modules/rds/aws"

  identifier = "pathminddb"

  engine            = "postgres"
  engine_version    = "9.6.9"
  instance_class    = "db.t2.large"
  allocated_storage = 100
  storage_encrypted = false
  multi_az          = true

  # kms_key_id        = "arm:aws:kms:<region>:<account id>:key/<kms key id>"
  name = "pathminddb"

  # NOTE: Do NOT use 'user' as the value for 'username' as it throws:
  # "Error creating DB Instance: InvalidParameterValue: MasterUsername
  # user cannot be used as it is a reserved word used by the engine"
  username = "demouser"

  password = var.dbpassword
  port     = "5432"

  vpc_security_group_ids = [aws_security_group.pathminddb.id]

  maintenance_window = "Mon:00:00-Mon:03:00"
  backup_window      = "03:00-06:00"

  backup_retention_period = 0

  tags = {
    Name       = "pathminddb"
  }

  enabled_cloudwatch_logs_exports = ["postgresql", "upgrade"]

  # DB subnet group
  subnet_ids = data.aws_subnet_ids.all.ids

  # DB parameter group
  family = "postgres9.6"

  # DB option group
  major_engine_version = "11.5"

  # Snapshot name upon DB deletion
  final_snapshot_identifier = "pathminddb"

  # Database Deletion Protection
  deletion_protection = false
}
