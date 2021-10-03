variable "gcp_project" {
  type        = string
  description = "Google Cloud Project hosting the Dumpling App deployment"
}

variable "gcp_region" {
  type        = string
  default     = "europe-west1"
  description = "Google Cloud Platform region hosting the Dumpling App"
}

variable "gcp_zone" {
  type        = string
  default     = "europe-west1-b"
  description = "Google Cloud Platform zone hosting the Dumpling App"
}

variable "service_account_id" {
  type        = string
  description = "SA used to manage the Dumpling App resources"
}

variable "gcs_bucket" {
  type        = string
  description = "Permanent bucket containing tf state, disk backup, certificate etc..."
}