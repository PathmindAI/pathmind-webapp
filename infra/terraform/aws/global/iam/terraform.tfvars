environment = "staging"
region      = "us-east-1"

# Name of the state bucket to create
s3_bucket = "pathmind-terraform-state"

# List of user emails that will be allowed to use the project admin service account
# user:xyz or serviceaccount:xyz
#admin_user_list = ["user:iponimansky@opsline.com", "user:josiecki@opsline.com", "user:hliang@opsline.com"]

# List of roles or permissions to add to the GKE service account
#additional_gke_service_account_roles=[]
