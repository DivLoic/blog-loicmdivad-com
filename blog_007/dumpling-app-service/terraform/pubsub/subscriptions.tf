resource "google_pubsub_subscription" "test-command" {
  name       = "test-command"
  depends_on = [google_pubsub_topic.commands]
  topic      = google_pubsub_topic.commands.name

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

  enable_message_ordering = false
}