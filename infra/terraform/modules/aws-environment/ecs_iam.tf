# used to dynamically get AWS Account ID


resource "aws_iam_role" "ecs_task_execution_role" {
  name = "${var.environment}-ecs_task_execution_role"
  path = "/"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": [ "ecs-tasks.amazonaws.com" ]
      },
      "Action": [ "sts:AssumeRole" ]
    }
  ]
}
EOF


  tags = {
    Terraform = "true"
  }
}

resource "aws_iam_role_policy_attachment" "ecs-task-execution-role-policy-attach" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role" "ecs_task_role" {
  name = "${var.environment}-ecs_task_role"
  path = "/"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": [ "ecs-tasks.amazonaws.com" ]
      },
      "Action": [ "sts:AssumeRole" ]
    }
  ]
}
EOF


  tags = {
    Terraform = "true"
  }
}

data "aws_iam_policy_document" "kms_read_policy_document" {
  statement {
    effect = "Allow"
    actions = [
      "kms:Get*",
      "kms:List*",
      "kms:Decrypt",
      "kms:Describe*",
    ]
    resources = [module.infra.kms_key_arn]
  }
}

resource "aws_iam_policy" "kms_read_policy" {
  name        = "${var.environment}-kms_read_policy"
  path        = "/"
  description = "Grants access to read kms keys for ${var.environment} environment."
  policy      = data.aws_iam_policy_document.kms_read_policy_document.json
}

data "aws_iam_policy_document" "secrets_read_policy" {
  statement {
    effect = "Allow"
    actions = [
      "ssm:GetParameters",
      "ssm:DescribeParameters",
    ]
    resources = ["arn:aws:ssm:${var.region}:${data.aws_caller_identity.current.account_id}:parameter/${var.environment}/*"]
  }
}

resource "aws_iam_policy" "secrets_read" {
  name   = "${var.environment}-ssm-read"
  policy = data.aws_iam_policy_document.secrets_read_policy.json
}

resource "aws_iam_role_policy_attachment" "ecs_task_secrets_read_attachment" {
  role       = aws_iam_role.ecs_task_role.name
  policy_arn = aws_iam_policy.secrets_read.arn
}

resource "aws_iam_role_policy_attachment" "ecs_task_kms_policy_attach" {
  role       = aws_iam_role.ecs_task_role.name
  policy_arn = aws_iam_policy.kms_read_policy.arn
}

resource "aws_iam_role_policy_attachment" "execution_role_read_ssm" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = aws_iam_policy.secrets_read.arn
}

resource "aws_iam_role_policy_attachment" "execution_role_read_kms" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = aws_iam_policy.kms_read_policy.arn
}

