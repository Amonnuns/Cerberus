variable "resources_location" {
    default = "brazilsouth"
}

variable resource_group_name {
    default = "cerberusResourceGroup"
}

variable cluster_name {
    default = "k8scerberus"
}

variable "dns_prefix" {
    default = "k8scerberus"
}

variable "ssh_public_key" {
  default = "../mykey.pub"
}

variable "agent_count" {
    default = 2
}

variable "aks_service_principal_app_id"{

}

variable "aks_service_principal_client_secret" {
  
}

variable "aks_service_principal_object_id" {
  
}