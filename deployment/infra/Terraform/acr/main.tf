terraform {

  backend "azurerm" {
    resource_group_name  = "wipcontrol-rg"
    storage_account_name = "wipcontrolsa"
    container_name       = "tfstate"
    key                  = "wipcontrol.terraform.tfstate"
  }
}

provider "azurerm" {
  features {}
}

# resource "azurerm_resource_group" "acr_resource_group" {
#   name     = var.resource_group_name
#   location = var.location

# #   tags = var.tags

# }

resource "azurerm_container_registry" "acr" {
  name                = var.name
  resource_group_name = azurerm_resource_group.acr_resource_group.name
  location            = azurerm_resource_group.acr_resource_group.location
  sku                 = "Standard"
  admin_enabled       = false

#   tags = var.tags

}