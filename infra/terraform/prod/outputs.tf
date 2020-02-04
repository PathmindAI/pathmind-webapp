### RDS ###
output "db_instance_endpoint" {
  description = "The connection endpoint"
  value       = "${aws_db_instance.rds.endpoint}"
}

output "db_name" {
  description = "db name"
  value       = "${aws_db_instance.rds.name}"
}

output "db_username" {
  description = "username"
  value       = "${aws_db_instance.rds.username}"
}
