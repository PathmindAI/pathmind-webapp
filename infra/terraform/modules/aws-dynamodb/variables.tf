variable "region" {
}

variable "name" {
}

variable "hash_key" {
}

variable "billing_mode" {
  default = "PROVISIONED"
}

variable "read_capacity" {
}

variable "write_capacity" {
}

variable "attributes" {
  description = "A mapping of attributes"
  type = list(object({ name = string, type = string }))
}

