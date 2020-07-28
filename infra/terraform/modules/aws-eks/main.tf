data "aws_vpc" "selected" {
  filter {
    name   = "tag:Name"
    values = [var.environment]
  }
}

data "aws_subnet_ids" "selected" {
  vpc_id = data.aws_vpc.selected.id
  filter {
    name   = "tag:Name"
    values = ["${var.environment}-private-*"]
  }
}

data "aws_eks_cluster" "cluster" {
  name = module.cluster.cluster_id
}

data "aws_eks_cluster_auth" "cluster" {
  name = module.cluster.cluster_id
}

provider "kubernetes" {
  host                   = data.aws_eks_cluster.cluster.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority.0.data)
  token                  = data.aws_eks_cluster_auth.cluster.token
  load_config_file       = false
  version                = "~> 1.9"
}

module "cluster" {
  source          = "terraform-aws-modules/eks/aws"
  cluster_name    = var.cluster_name
  cluster_version = var.cluster_version
  subnets         = data.aws_subnet_ids.selected.ids
  vpc_id          = data.aws_vpc.selected.id

  worker_groups_launch_template = [
    {
      name                    = "SPOT-16cpu_32gb"
      override_instance_types = ["a1.4xlarge", "a1.metal", "c5.4xlarge", "c5d.4xlarge", "c5n.4xlarge"]
      spot_instance_pools     = 5
      asg_min_size            = 0
      asg_max_size            = 50
      asg_desired_capacity    = 0
      kubelet_extra_args      = "--node-labels=node.kubernetes.io/lifecycle=spot --node-labels=dedicated=SPOT-16cpu_32gb --register-with-taints=dedicated=SPOT-16cpu_32gb:NoSchedule"
      tags = [
        {
          "key"                 = "k8s.io/cluster-autoscaler/enabled"
          "propagate_at_launch" = "false"
          "value"               = "true"
        },
        {
          "key"                 = "k8s.io/cluster-autoscaler/${var.cluster_name}"
          "propagate_at_launch" = "false"
          "value"               = "true"
        }
      ]
    },
    {
      name                    = "SPOT-16cpu_64gb"
      override_instance_types = ["g4dn.4xlarge", "h1.4xlarge", "m4.4xlarge", "m5.4xlarge", "m5a.4xlarge", "m5ad.4xlarge", "m5d.4xlarge", "m5n.4xlarge", "m5dn.4xlarge"]
      spot_instance_pools     = 9
      asg_min_size            = 0
      asg_max_size            = 50
      asg_desired_capacity    = 0
      kubelet_extra_args      = "--node-labels=node.kubernetes.io/lifecycle=spot --node-labels=dedicated=SPOT-16cpu_64gb --register-with-taints=dedicated=SPOT-16cpu_64gb:NoSchedule"
      tags = [
        {
          "key"                 = "k8s.io/cluster-autoscaler/enabled"
          "propagate_at_launch" = "false"
          "value"               = "true"
        },
        {
          "key"                 = "k8s.io/cluster-autoscaler/${var.cluster_name}"
          "propagate_at_launch" = "false"
          "value"               = "true"
        }
      ]
    },
    {
      name                    = "SPOT-36cpu_72gb"
      override_instance_types = ["c4.8xlarge", "c5.9xlarge", "c5d.9xlarge", "c5n.9xlarge"]
      spot_instance_pools     = 4
      asg_min_size            = 1
      asg_max_size            = 50
      asg_desired_capacity    = 1
      kubelet_extra_args      = "--node-labels=node.kubernetes.io/lifecycle=spot --node-labels=dedicated=SPOT-36cpu_72gb --register-with-taints=dedicated=SPOT-36cpu_72gb:NoSchedule"
      tags = [
        {
          "key"                 = "k8s.io/cluster-autoscaler/enabled"
          "propagate_at_launch" = "false"
          "value"               = "true"
        },
        {
          "key"                 = "k8s.io/cluster-autoscaler/${var.cluster_name}"
          "propagate_at_launch" = "false"
          "value"               = "true"
        }
      ]
    },
    {
      name                    = "SPOT-8cpu_16gb"
      override_instance_types = ["a1.2xlarge", "c5.2xlarge", "c5d.2xlarge", "c5n.2xlarge", "m3.2xlarge"]
      spot_instance_pools     = 5
      asg_min_size            = 0
      asg_max_size            = 50
      asg_desired_capacity    = 0
      kubelet_extra_args      = "--node-labels=node.kubernetes.io/lifecycle=spot --node-labels=dedicated=SPOT-8cpu_16gb --register-with-taints=dedicated=SPOT-8cpu_16gb:NoSchedule"
      tags = [
        {
          "key"                 = "k8s.io/cluster-autoscaler/enabled"
          "propagate_at_launch" = "false"
          "value"               = "true"
        },
        {
          "key"                 = "k8s.io/cluster-autoscaler/${var.cluster_name}"
          "propagate_at_launch" = "false"
          "value"               = "true"
        }
      ]
    },
    {
      name                    = "SPOT-8cpu_32gb"
      override_instance_types = ["m4.2xlarge", "m5.2xlarge", "t2.2xlarge", "t3.2xlarge"]
      spot_instance_pools     = 4
      asg_min_size            = 0
      asg_max_size            = 50
      asg_desired_capacity    = 0
      kubelet_extra_args      = "--node-labels=node.kubernetes.io/lifecycle=spot --node-labels=dedicated=SPOT-8cpu_32gb --register-with-taints=dedicated=SPOT-8cpu_32gb:NoSchedule"
      tags = [
        {
          "key"                 = "k8s.io/cluster-autoscaler/enabled"
          "propagate_at_launch" = "false"
          "value"               = "true"
        },
        {
          "key"                 = "k8s.io/cluster-autoscaler/${var.cluster_name}"
          "propagate_at_launch" = "false"
          "value"               = "true"
        }
      ]
    }
  ]

  worker_groups = [
    {
      name                 = "worker-group-1"
      instance_type        = var.instance_type
      asg_max_size         = var.asg_max_size
      asg_desired_capacity = var.asg_desired_capacity
      tags = [
        {
          "key"                 = "k8s.io/cluster-autoscaler/enabled"
          "propagate_at_launch" = "false"
          "value"               = "true"
        },
        {
          "key"                 = "k8s.io/cluster-autoscaler/${var.cluster_name}"
          "propagate_at_launch" = "false"
          "value"               = "true"
        }
      ]
    }
  ]
}
