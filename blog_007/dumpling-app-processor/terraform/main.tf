provider "google" {
  project      = var.gcp_project
  region       = var.gcp_region
  access_token = data.google_service_account_access_token.default.access_token
}

locals {
  inputTopic = "projects/${var.gcp_project}/topics/dumpling-commands"
  inputTopicSubscription = "projects/${var.gcp_project}/subscriptions/${var.topic_subscription}"
}

resource "google_dataflow_job" "processor" {
  name = "dumpling-app-processor"
  depends_on = [google_pubsub_subscription.processor-subscription]
  enable_streaming_engine = true
  template_gcs_path = var.dataflow_template_location
  temp_gcs_location = var.dataflow_gcs_location
  parameters = {

    datastoreProject = var.gcp_project
    inputTopicSubscription    = local.inputTopicSubscription
  }

  on_delete = "cancel"
}

resource "google_pubsub_subscription" "processor-subscription" {
  name       = var.topic_subscription
  topic      = local.inputTopic

  # 20 minutes
  message_retention_duration = "1200s"
  retain_acked_messages      = true

  ack_deadline_seconds = 20

  expiration_policy {
    ttl = "300000.5s"
  }
  retry_policy {
    minimum_backoff = "10s"
  }

  enable_message_ordering = true
}