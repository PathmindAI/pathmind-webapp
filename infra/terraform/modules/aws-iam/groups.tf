#######
# Groups
#######
resource "aws_iam_group" "admins" {
  name = "admins"
}

resource "aws_iam_group_policy_attachment" "admins-mfa" {
  group      = aws_iam_group.admins.name
  policy_arn = aws_iam_policy.allow-manage-mfa-policy.arn
}

resource "aws_iam_group_policy_attachment" "admins-admins" {
  group      = aws_iam_group.admins.name
  policy_arn = aws_iam_policy.admins-policy.arn
}

resource "aws_iam_group_policy_attachment" "admins-state" {
  group      = aws_iam_group.admins.name
  policy_arn = aws_iam_policy.admins-state-bucket.arn
}

resource "aws_iam_group" "devs" {
  name = "devs"
}

resource "aws_iam_group_policy_attachment" "devs-mfa" {
  group      = aws_iam_group.devs.name
  policy_arn = aws_iam_policy.allow-manage-mfa-policy.arn
}

resource "aws_iam_group_policy_attachment" "devs-devs" {
  group      = aws_iam_group.devs.name
  policy_arn = aws_iam_policy.devs-policy.arn
}

resource "aws_iam_group" "operators" {
  name = "operators"
}

resource "aws_iam_group_policy_attachment" "operators-mfa" {
  group      = aws_iam_group.operators.name
  policy_arn = aws_iam_policy.allow-manage-mfa-policy.arn
}

resource "aws_iam_group_policy_attachment" "operators-devs" {
  group      = aws_iam_group.operators.name
  policy_arn = aws_iam_policy.operators-policy.arn
}

