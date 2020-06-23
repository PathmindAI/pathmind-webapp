#################
# Password Policy
#################

# TODO update password policy below with more secure settings (see commented-out values)
resource "aws_iam_account_password_policy" "password-policy" {
  minimum_password_length        = 8 #15
  require_lowercase_characters   = true
  require_numbers                = true
  require_uppercase_characters   = true
  require_symbols                = true
  allow_users_to_change_password = true
  #max_password_age               = 90
  #password_reuse_prevention      = 5
}

