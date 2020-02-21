module "pathmind_test" {
  source="./modules/pathmind/"
  environment="test"
  DB_URL="${var.DB_URL_TEST}"
  DB_URL_CLI="${var.DB_URL_CLI_TEST}"
  awsaccesskey="${var.awsaccesskey}"
  awssecretaccesskey="${var.awssecretaccesskey}"
  SEGMENT_WEBSITE_KEY="${var.SEGMENT_WEBSITE_KEY}"
  SEGMENT_SERVER_KEY="${var.SEGMENT_SERVER_KEY}"
  node_subnet_ids="${module.kubernetes.node_subnet_ids}"
  vpc_id="${module.kubernetes.vpc_id}"
  db_allocated_storage="${var.db_allocated_storage}"
  db_instance_class="${var.db_instance_class}"
  multi_az="${var.multi_az}"
  database_name="${var.database_name}"
  database_username="${var.database_username}"
  database_password="${var.database_password}"
}

