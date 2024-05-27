terraform {
  backend "azurerm" {
    resource_group_name  = "wipcontrol-rg"
    storage_account_name = "wipcontrolsa"
    container_name       = "tfstate-aks"
    key                  = "wipcontrol.terraform.tfstate"
  }
}

terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
    }
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
resource "azurerm_kubernetes_cluster" "k8squickstart" {
  name                = var.name
  location            = var.location
  resource_group_name = var.resource_group_name
  dns_prefix          = "${var.name}-dns01"

  kubernetes_version = var.k8s_version

  
  network_profile {
  network_plugin = "azure"
  network_policy = "azure"
}

  default_node_pool {
    name       = "default"
    node_count = var.node_count
    vm_size    = "Standard_A2_v2"
    
  }

  identity {
    type = "SystemAssigned"
  }

  tags = {
    environment = "Development"
  }
}

resource "azurerm_role_assignment" "acr_pull" {
  principal_id           = azurerm_kubernetes_cluster.k8squickstart.identity[0].principal_id
  role_definition_name   = "AcrPull"
  scope                  = azurerm_container_registry.acr.id
}