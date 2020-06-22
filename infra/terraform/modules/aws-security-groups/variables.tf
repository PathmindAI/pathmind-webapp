variable "vpc_id" {
  type = string
}

variable "vpc_cidr_block" {
  type = string
}

variable "environment" {
  type = string
}

variable "tags" {
  type = map(string)
}

variable "trusted_networks" {
  type = list(string)
}

