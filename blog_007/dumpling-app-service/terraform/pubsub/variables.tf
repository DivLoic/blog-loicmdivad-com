variable "gcp_project" {
  type = string
  description = "Google Cloud Project hosting the Dumpling App deployment"
}

variable "gcp_zone" {
  type = string
  default = "europe-west1-b"
  description = "Google Cloud Platform zone hosting the Dumpling App"
}