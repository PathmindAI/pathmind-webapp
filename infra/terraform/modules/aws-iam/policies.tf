resource "aws_iam_policy" "ecr-read" {
  name        = "ecr-read"
  description = "ecr read policy"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ecr:BatchCheckLayerAvailability",
        "ecr:BatchGetImage",
        "ecr:DescribeImages",
        "ecr:DescribeRepositories",
        "ecr:GetAuthorizationToken",
        "ecr:GetDownloadUrlForLayer",
        "ecr:GetRepositoryPolicy",
        "ecr:ListImages"
      ],
        "Resource": "*"
    }
  ]
}
EOF

}

resource "aws_iam_policy" "ecr-write" {
  name        = "ecr-write"
  description = "ecr write policy"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "ecr:*",
      "Resource": "*"
    }
  ]
}
EOF

}

#################
# MFA Policy
#################
# This allows users to manage their own MFA and also will block most actions unless MFA is enabled

data "aws_iam_policy_document" "allow-manage-mfa" {
  statement {
    effect = "Allow"
    actions = [
      "iam:GetAccountPasswordPolicy",
      "iam:GetAccountSummary",
      "iam:ListAccountAliases",
      "iam:ListAttachedGroupPolicies",
      "iam:ListGroupPolicies",
      "iam:ListGroups",
      "iam:ListMFADevices",
      "iam:ListUsers",
      "iam:ListVirtualMFADevices",
    ]
    resources = [
      "*",
    ]
  }
  statement {
    effect = "Allow"
    actions = [
      "iam:ChangePassword",
      "iam:CreateAccessKey",
      "iam:CreateLoginProfile",
      "iam:DeleteAccessKey",
      "iam:DeleteLoginProfile",
      "iam:DeleteSigningCertificate",
      "iam:DeleteSSHPublicKey",
      "iam:GetLoginProfile",
      "iam:GetSSHPublicKey",
      "iam:GetUser",
      "iam:ListAccessKeys",
      "iam:ListAttachedUserPolicies",
      "iam:ListGroups",
      "iam:ListGroupsForUser",
      "iam:ListServiceSpecificCredentials",
      "iam:ListSigningCertificates",
      "iam:ListSSHPublicKeys",
      "iam:ListUserPolicies",
      "iam:UpdateAccessKey",
      "iam:UpdateLoginProfile",
      "iam:UpdateSigningCertificate",
      "iam:UpdateSSHPublicKey",
      "iam:UploadSigningCertificate",
      "iam:UploadSSHPublicKey",
    ]
    resources = [
      "arn:aws:iam::${data.aws_caller_identity.current.account_id}:user/user/$${aws:username}",
    ]
  }
  statement {
    effect = "Allow"
    actions = [
      "iam:CreateVirtualMFADevice",
      "iam:DeleteVirtualMFADevice",
      "iam:EnableMFADevice",
      "iam:ListMFADevices",
      "iam:ResyncMFADevice",
    ]
    resources = [
      "arn:aws:iam::${data.aws_caller_identity.current.account_id}:mfa/*",
      "arn:aws:iam::${data.aws_caller_identity.current.account_id}:user/user/$${aws:username}",
    ]
  }
  # Block actions unless MFA is present
  # statement {
  #   effect = "Deny"
  #   not_actions = [
  #     "iam:CreateVirtualMFADevice",
  #     "iam:DeleteVirtualMFADevice",
  #     "iam:ListVirtualMFADevices",
  #     "iam:EnableMFADevice",
  #     "iam:ResyncMFADevice",
  #     "iam:ListAccountAliases",
  #     "iam:ListUsers",
  #     "iam:ListSSHPublicKeys",
  #     "iam:ListAccessKeys",
  #     "iam:ListServiceSpecificCredentials",
  #     "iam:ListMFADevices",
  #     "iam:GetAccountSummary",
  #     "sts:GetSessionToken"
  #   ]

  #   condition = {
  #     test = "BoolIfExists"
  #     variable = "aws:MultiFactorAuthPresent"
  #     values = [
  #     "false",
  #     ]
  #   }
  #   resources = [
  #     "*"
  #   ]
  # }
}

resource "aws_iam_policy" "allow-manage-mfa-policy" {
  name        = "allow-manage-mfa"
  path        = "/"
  description = "Policy for allow users to manage MFA"
  policy      = data.aws_iam_policy_document.allow-manage-mfa.json
}

####################
# Operators policy
####################
resource "aws_iam_policy" "operators-policy" {
  name        = "operators"
  path        = "/"
  description = "Policy for Operators"

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": [
                "autoscaling:*",
                "cloudwatch:*",
                "dynamodb:*",
                "ec2:CancelSpotInstanceRequests",
                "ec2:CancelSpotFleetRequests",
                "ec2:CreateTags",
                "ec2:DeleteTags",
                "ec2:DescribeKeyPairs",
                "ec2:Describe*",
                "ec2:ModifyImageAttribute",
                "ec2:ModifyInstanceAttribute",
                "ec2:ModifySpotFleetRequest",
                "ec2:RequestSpotInstances",
                "ec2:RequestSpotFleet",
                "ec2:*Volume",
                "ec2:*Snapshot",
                "ec2:*Image",
                "ec2:RebootInstances",
                "ec2:StopInstances",
                "ec2:StartInstances",
                "elasticfilesystem:*",
                "elasticmapreduce:*",
                "es:*",
                "firehose:*",
                "iam:GetInstanceProfile",
                "iam:GetRole",
                "iam:GetPolicy",
                "iam:GetPolicyVersion",
                "iam:ListRoles",
                "iam:ListInstance*",
                "kinesis:*",
                "kinesisanalytics:*",
                "kinesisvideo:*",
                "kms:List*",
                "lambda:Create*",
                "lambda:Delete*",
                "lambda:Get*",
                "lambda:InvokeFunction",
                "lambda:PublishVersion",
                "lambda:Update*",
                "lambda:List*",
                "machinelearning:*",
                "sdb:*",
                "rds:*",
                "sns:ListSubscriptions",
                "sns:ListTopics",
                "logs:DescribeLogStreams",
                "logs:GetLogEvents",
                "redshift:*",
                "sns:CreateTopic",
                "sns:Get*",
                "sns:List*",
                "sagemaker:*"
            ],
            "Effect": "Allow",
            "Resource": "*"
        },
        {
            "Effect": "Deny",
            "Action": [
                "organizations:*",
                "ec2:*Acl",
                "ec2:*Acl*"
            ],
            "Resource": "*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "s3:Abort*",
                "s3:DeleteObject",
                "s3:Get*",
                "s3:List*",
                "s3:PutAccelerateConfiguration",
                "s3:PutBucketLogging",
                "s3:PutBucketNotification",
                "s3:PutBucketTagging",
                "s3:PutObject",
                "s3:Replicate*",
                "s3:RestoreObject"
            ],
            "Resource": [
                "*"
            ]
        },
        {
            "Effect": "Allow",
            "Action": [
                "ec2:TerminateInstances"
            ],
            "Resource": [
                "*"
            ]
        },
        {
          "Action": "execute-api:*",
          "Effect": "Deny",
          "Resource": "*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "iam:GetRole",
                "iam:PassRole"
            ],
            "Resource": [
                "*"
            ]
        },
        {
            "Effect": "Allow",
            "Action": [
                "s3:ListAllMyBuckets",
                "s3:HeadBucket"
            ],
            "Resource": "*"
        },
        {
            "Effect": "Deny",
            "Action": "s3:*",
            "Resource": [
                "arn:aws:s3:::${var.s3_bucket}",
                "arn:aws:s3:::${var.s3_bucket}/*"
            ]
        }
    ]
}
EOF

}

#################
# Devs policy
#################
resource "aws_iam_policy" "devs-policy" {
  name        = "devs"
  path        = "/"
  description = "Policy for Devs"

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": [
                "autoscaling:*",
                "cloudwatch:*",
                "dynamodb:*",
                "ec2:CancelSpotInstanceRequests",
                "ec2:CancelSpotFleetRequests",
                "ec2:CreateTags",
                "ec2:DeleteTags",
                "ec2:DescribeKeyPairs",
                "ec2:Describe*",
                "ec2:ModifyImageAttribute",
                "ec2:ModifyInstanceAttribute",
                "ec2:ModifySpotFleetRequest",
                "ec2:RequestSpotInstances",
                "ec2:RequestSpotFleet",
                "ec2:*Volume",
                "ec2:*Snapshot",
                "ec2:*Image",
                "ec2:RebootInstances",
                "ec2:StopInstances",
                "ec2:StartInstances",
                "elasticfilesystem:*",
                "elasticmapreduce:*",
                "es:*",
                "firehose:*",
                "iam:GetInstanceProfile",
                "iam:GetRole",
                "iam:GetPolicy",
                "iam:GetPolicyVersion",
                "iam:ListRoles",
                "iam:ListInstance*",
                "kinesis:*",
                "kinesisanalytics:*",
                "kinesisvideo:*",
                "kms:List*",
                "lambda:Create*",
                "lambda:Delete*",
                "lambda:Get*",
                "lambda:InvokeFunction",
                "lambda:PublishVersion",
                "lambda:Update*",
                "lambda:List*",
                "machinelearning:*",
                "sdb:*",
                "rds:*",
                "sns:ListSubscriptions",
                "sns:ListTopics",
                "logs:DescribeLogStreams",
                "logs:GetLogEvents",
                "redshift:*",
                "sns:CreateTopic",
                "sns:Get*",
                "sns:List*",
                "sagemaker:*"
            ],
            "Effect": "Allow",
            "Resource": "*"
        },
        {
            "Effect": "Deny",
            "Action": [
                "organizations:*",
                "ec2:*Acl",
                "ec2:*Acl*"
            ],
            "Resource": "*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "s3:Abort*",
                "s3:DeleteObject",
                "s3:Get*",
                "s3:List*",
                "s3:PutAccelerateConfiguration",
                "s3:PutBucketLogging",
                "s3:PutBucketNotification",
                "s3:PutBucketTagging",
                "s3:PutObject",
                "s3:Replicate*",
                "s3:RestoreObject"
            ],
            "Resource": [
                "*"
            ]
        },
        {
            "Effect": "Allow",
            "Action": [
                "ec2:TerminateInstances"
            ],
            "Resource": [
                "*"
            ]
        },
        {
          "Action": "execute-api:*",
          "Effect": "Deny",
          "Resource": "*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "iam:GetRole",
                "iam:PassRole"
            ],
            "Resource": [
                "*"
            ]
        },
        {
            "Effect": "Allow",
            "Action": [
                "s3:ListAllMyBuckets",
                "s3:HeadBucket"
            ],
            "Resource": "*"
        },
        {
            "Effect": "Deny",
            "Action": "s3:*",
            "Resource": [
                "arn:aws:s3:::${var.s3_bucket}",
                "arn:aws:s3:::${var.s3_bucket}/*"
            ]
        }
    ]
}
EOF

}

#################
# Admins policy
#################

resource "aws_iam_policy" "admins-policy" {
  name        = "admins"
  path        = "/"
  description = "Policy for Admins"

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "*",
            "Resource": "*"
        }
    ]
}
EOF

}

################
# Role Policies
################

# default instance role policy
data "aws_iam_policy_document" "default_role_policy" {
  statement {
    actions = ["sts:AssumeRole"]

    principals {
      type        = "Service"
      identifiers = ["ec2.amazonaws.com"]
    }
  }
}

data "aws_iam_policy" "aws_managed_ssm_policy" {
  arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2RoleforSSM"
}

# Allow admins to manage state bucket
resource "aws_iam_policy" "admins-state-bucket" {
  name        = "admins-state-bucket"
  path        = "/"
  description = "Allow admins to manage state bucket"

  policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AllowAdminstate",
      "Effect": "Allow",
      "Action": "s3:*",
      "Resource": [
        "arn:aws:s3:::${var.s3_bucket}/*",
        "arn:aws:s3:::${var.s3_bucket}"
      ]
    }
  ]
}
POLICY

}

