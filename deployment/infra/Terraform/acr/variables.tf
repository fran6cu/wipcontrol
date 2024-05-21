variable "name" {
  type        = string
  default     = "wipcontrolacr"
  description = "Name for resources"
}

variable "location" {
  type        = string
  default     = "westeurope"
  description = "Azure Location of resources"
}

# variable "tags" {
#   type = map(string)
# }

variable "resource_group_name" {
  type = string
  default = "wipcontrol-rg"
}