# Force_destroy is necessary since keys are not managed by TF, e.g.
#
# resource "aws_iam_user" "user_name" {
#   name = "user_name"
#   path = "/user/"
#   force_destroy = true
# }
# resource "aws_iam_user_group_membership" "example1" {
#   user = "${aws_iam_user.user_name.name}"
#   groups = [
#     "${module.aws-iam.admin}",
#     "${module.aws-iam.other-group}",
#   ]
# }
