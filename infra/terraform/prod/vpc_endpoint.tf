resource "aws_vpc_endpoint" "s3" {
  vpc_id       = "${var.vpc_id}"
  service_name = "com.amazonaws.${var.region}.s3"
}
