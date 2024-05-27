terraform {

  backend "azurerm" {
    resource_group_name  = "wipcontrol-rg"
    storage_account_name = "wipcontrolsa"
    container_name       = "tfstate-acr"
    key                  = "wipcontrol.terraform.tfstate"
  }
}

provider "azurerm" {
  features {}
}


resource "azurerm_container_registry" "acr" {
  name                = var.name
  resource_group_name = var.resource_group_name
  location            = var.location
  sku                 = "Standard"
  admin_enabled       = false

 tags = {
    environment = "Development"
  }

}

