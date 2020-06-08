variable "environment" {
  type = string
}

variable "vpc_cidr" {
  type = string
}

variable "vpc_azs" {
  type = list(string)
}

variable "private_subnets" {
  type = list(string)
}

variable "private_subnet_tags" {
  description = "Additional tags for the public subnets"
  default     = {}
}

variable "public_subnets" {
  type = list(string)
}

variable "public_subnet_tags" {
  description = "Additional tags for the public subnets"
  default     = {}
}

variable "single_nat_gateway" {
  type    = string
  default = false
}

variable "create_external_ssh_sg" {
  default = true
}

variable "create_external_web_sg" {
  default = true
}

variable "create_internal_ssh_sg" {
  default = true
}

variable "create_internal_web_sg" {
  default = true
}

variable "create_internal_ecs_sg" {
  default = true
}

variable "create_vpn_sg" {
  default = false
}

variable "create_efs_sg" {
  default = false
}

variable "create_jenkins_sg" {
  default = false
}

variable "trusted_networks" {
  type = list(string)
}

variable "allow_public_ingress" {
  description = "If true, this will create ACL rules to allow web traffic. Required if you want any outbound connections"
  default     = false
}

variable "enable_dhcp_options" {
  description = "Should be true if you want to specify a DHCP options set with a custom domain name, DNS servers, NTP servers, netbios servers, and/or netbios server type"
  default     = false
}

variable "dhcp_options_domain_name_servers" {
  description = "Specify a list of DNS server addresses for DHCP options set, default to AWS provided"
  type        = list(string)
  default     = ["AmazonProvidedDNS"]
}