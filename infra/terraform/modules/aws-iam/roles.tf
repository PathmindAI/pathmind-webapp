# default instance role and profile
resource "aws_iam_role" "default_instance_role" {
  description           = "The default role instances can use"
  name                  = "DefaultInstanceRole"
  force_detach_policies = true
  assume_role_policy    = data.aws_iam_policy_document.default_role_policy.json
}

resource "aws_iam_instance_profile" "default_instance_profile" {
  name = "default_instance_profile"
  role = aws_iam_role.default_instance_role.name
}

# attach SSM policy to default role
resource "aws_iam_role_policy_attachment" "default_instance_profile_ssm_attach" {
  role       = aws_iam_role.default_instance_role.name
  policy_arn = data.aws_iam_policy.aws_managed_ssm_policy.arn
}


