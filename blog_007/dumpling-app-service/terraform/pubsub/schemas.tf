resource "google_pubsub_schema" "checkout" {
  name = "dumpling-checkout"
  type = "PROTOCOL_BUFFER"
  definition =  file("${path.module}/../../src/main/proto/checkout.proto")
}

resource "google_pubsub_schema" "commands" {
  name = "dumpling-commands"
  type = "PROTOCOL_BUFFER"
  definition = file("${path.module}/../../src/main/proto/command.proto")
}