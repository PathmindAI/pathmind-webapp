data "aws_network_acls" "public" {
  vpc_id = module.infra.vpc_id
  filter {
    name = "association.subnet-id"
    values = [element(module.infra.public_subnets, 0)]
  }
}


#data "template_file" "user_data" {
#  template = file("${path.module}/templates/user-data.sh")
#
#  vars = {
#    cluster_name             = module.infra.ecs_cluster_name
#    hosted_zone_name         = var.hosted_zone_name
#  }
#}


# EC2 ECS resources
data "aws_ami" "amazon_linux_2_ecs" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["amzn2-ami-ecs-hvm-*-x86_64-ebs"]
  }
}