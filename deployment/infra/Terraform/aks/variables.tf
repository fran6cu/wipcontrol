variable "name" {
  type = string
  default = "wipcontrolacr"
}

variable "resource_group_name" {
  type = string
  default = "wipcontrol-rg"
}

variable "location" {
  type = string
  default = "westeurope"
}

variable "node_count" {
  type = string
  default = 3
}

  variable "k8s_version" {
    type = string
    default = "1.28.5" # https://kubernetes.io/releases/ -- End of life 28/10/2024
  }