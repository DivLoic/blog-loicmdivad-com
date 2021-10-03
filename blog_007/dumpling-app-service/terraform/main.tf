provider "google" {
  project      = var.gcp_project
  region       = var.gcp_region
  access_token = data.google_service_account_access_token.default.access_token
}

module "pubsub" {
  source      = "./pubsub"
  gcp_zone    = var.gcp_zone
  gcp_project = var.gcp_project
}