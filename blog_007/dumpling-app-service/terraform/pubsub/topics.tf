resource "google_pubsub_topic" "checkout" {
  name       = "dumpling-checkout"
  project    = data.google_project.main.name
  depends_on = [google_pubsub_schema.checkout]

  schema_settings {
    schema   = "projects/${var.gcp_project}/schemas/dumpling-checkout"
    encoding = "BINARY"
  }
}

resource "google_pubsub_topic" "commands" {
  name       = "dumpling-commands"
  project    = data.google_project.main.number
  depends_on = [google_pubsub_schema.commands]

  schema_settings {
    schema   = "projects/${var.gcp_project}/schemas/dumpling-commands"
    encoding = "BINARY"
  }
}