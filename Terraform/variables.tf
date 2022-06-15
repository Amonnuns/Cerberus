variable "resources_location" {
    default = "brazilsouth"
}

variable resource_group_name {
    default = "cerberusResourceGroup"
}

variable "container_registry_name" {
  default = "ACRCerberus"
}

variable cluster_name {
    default = "k8scerberus"
}

variable "dns_prefix" {
    default = "k8scerberus"
}

variable "storage_name" {
    default = "cerberusrage"
}
variable "pool_name" {
    default = "cerberuspool"
}

variable "vm_size" {
    default = "Standard_F4s_v2"

}
variable "ssh_public_key" {
  default = "../mykey.pub"
}

variable "agent_count" {
    default = 1
}

variable "aks_service_principal_app_id"{

}

variable "aks_service_principal_client_secret" {
  
}

variable "aks_service_principal_object_id" {
  
}