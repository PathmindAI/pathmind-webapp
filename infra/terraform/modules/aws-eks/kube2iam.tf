resource "aws_iam_role_policy_attachment" "kube2iam" {
  policy_arn = aws_iam_policy.kube2iam_policy.arn
  role       = module.cluster.worker_iam_role_name
}

resource "aws_iam_policy" "kube2iam_policy" {
  name        = "kube2iam_policy-${var.cluster_name}"
  path        = "/"
  description = "kube2iam_policy"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "sts:AssumeRole"
      ],
      "Effect": "Allow",
      "Resource": "*"
    }
  ]
}
EOF
}

resource "aws_iam_role" "eks" {
  name = "eks-${var.cluster_name}"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "ec2.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    },
    {
      "Sid": "",
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::${data.aws_caller_identity.current.account_id}:role/${module.cluster.worker_iam_role_name}"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "eks-eks" {
  role       = aws_iam_role.eks.name
  policy_arn = aws_iam_policy.eks-policy.arn
}

resource "aws_iam_policy" "eks-policy" {
  name        = "eks-policy-${var.cluster_name}"
  path        = "/"
  description = "Policy for eks"

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": [
                "ecr:*",
                "s3:*",
                "sns:*",
                "sqs:*",
                "dynamodb:*"
            ],
            "Effect": "Allow",
            "Resource": "*"
        }
    ]
}
EOF
}
