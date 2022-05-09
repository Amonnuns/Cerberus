terraform {
    required_providers{
        azurerm  = {
            source = "hashicorp/azurerm"
            version = "~> 2.65"
        }
    }

    required_version = ">= 0.13"
}

provider "azurerm" {
  features {}
}

resource "azurerm_resource_group" "rg" {
  name     = var.resource_group_name
  location = var.resources_location

  tags = {
    environment = "Cerberus ENV"
  }
}

resource "azurerm_kubernetes_cluster" "k8s" {
  name = var.cluster_name
  location = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name
  dns_prefix = var.dns_prefix

  linux_profile {
      admin_username = "cerberus"

      ssh_key {
          key_data = file(var.ssh_public_key)
          }
  }

  default_node_pool {
      name            = "cerberuspool"
      node_count      = var.agent_count
      vm_size         = "Standard_F4s_v2"
  }

  service_principal {
        client_id     = var.aks_service_principal_app_id
        client_secret = var.aks_service_principal_client_secret
    }

  network_profile {
    load_balancer_sku = "Standard"
    network_plugin = "kubenet"
  }

  tags = {
      Environment = "Cerberus ENV"
  }
}