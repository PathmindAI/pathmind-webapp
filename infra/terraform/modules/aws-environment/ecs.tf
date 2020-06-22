# IAM resources required for Admin ECS EC2 cluster
resource "aws_iam_role" "environment_ecs" {
  name = "${var.environment}_ecs_instance_role"
  path = "/ecs/"

  assume_role_policy = <<EOF
{
  "Version": "2008-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": ["ec2.amazonaws.com"]
      },
      "Effect": "Allow"
    }
  ]
}
EOF

}

resource "aws_iam_policy" "aws_ssm_extra_policy" {
  name        = "${var.environment}_ecs_ssm_extra_policy"
  description = "ecs ssm permissions beyond managed policy"

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": "ssm:GetParametersByPath",
            "Resource": "arn:aws:ssm:us-east-1:037673196406:parameter/*"
        }
    ]
}
EOF

}

resource "aws_iam_instance_profile" "environment_ecs" {
  name = "${var.environment}_ecs_instance_profile"
  role = aws_iam_role.environment_ecs.name
}

resource "aws_iam_role_policy_attachment" "ecs_ec2_role" {
  role       = aws_iam_role.environment_ecs.id
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceforEC2Role"
}

data "aws_iam_policy" "aws_managed_ssm_policy" {
  arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2RoleforSSM"
}

resource "aws_iam_role_policy_attachment" "ecs_ec2_ssm_policy" {
  role       = aws_iam_role.environment_ecs.id
  policy_arn = data.aws_iam_policy.aws_managed_ssm_policy.arn
}

resource "aws_iam_role_policy_attachment" "ecs_ec2_cloudwatch_role" {
  role       = aws_iam_role.environment_ecs.id
  policy_arn = "arn:aws:iam::aws:policy/CloudWatchLogsFullAccess"
}

resource "aws_iam_role_policy_attachment" "ecs_ec2_ssm_extra_policy" {
  role       = aws_iam_role.environment_ecs.id
  policy_arn = aws_iam_policy.aws_ssm_extra_policy.arn
}

# EC2 ECS resources
data "aws_ami" "amazon_linux_2_ecs" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["amzn2-ami-ecs-hvm-*-x86_64-ebs"]
  }
}

module "autoscaling" {
  source                       = "../aws-autoscaling"
  name                         = module.infra.ecs_cluster_name
  recreate_asg_when_lc_changes = true

  # Launch configuration
  lc_name              = "${module.infra.ecs_cluster_name}-lc"
  image_id             = data.aws_ami.amazon_linux_2_ecs.id
  instance_type        = var.ecs_instance_type
  security_groups      = [module.infra.internal_ecs_sg_id]
  iam_instance_profile = aws_iam_instance_profile.environment_ecs.name
  user_data            = data.template_file.user_data.rendered
  key_name             = var.key_pair_name
  environment          = var.environment
  enable_autoscaling   = var.enable_autoscaling

  # Auto scaling group
  asg_name                    = "${module.infra.ecs_cluster_name}-asg"
  vpc_zone_identifier         = module.infra.private_subnets
  associate_public_ip_address = false
  health_check_type           = "EC2"
  desired_capacity            = var.desired_capacity
  min_size                    = 1
  max_size                    = 15

  tags_as_map = {
    Name        = module.infra.ecs_cluster_name
    Environment = var.environment
    Cluster     = module.infra.ecs_cluster_name
    Terraform   = "true"
  }
}

data "template_file" "user_data" {
  template = file("${path.module}/templates/user-data.sh")

  vars = {
    cluster_name = module.infra.ecs_cluster_name
  }
}