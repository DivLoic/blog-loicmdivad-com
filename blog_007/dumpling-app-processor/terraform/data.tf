# service account token generation
provider "google" {
  alias = "token-access"
  scopes = [
    "https://www.googleapis.com/auth/cloud-platform",
    "https://www.googleapis.com/auth/userinfo.email",
  ]
}

data "google_service_account" "tf_account" {
  provider   = google.token-access
  project    = var.gcp_project
  account_id = var.service_account_id
}

data "google_service_account_access_token" "default" {
  provider               = google.token-access
  target_service_account = data.google_service_account.tf_account.email
  scopes = [
    "userinfo-email",
    "cloud-platform"
  ]
  lifetime = "3000s"
}

# available load balancer ip addresses
data "google_compute_lb_ip_ranges" "ranges" {}
