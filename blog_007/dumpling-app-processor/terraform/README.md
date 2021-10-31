# Dumpling App Processor

###### Terraform manifest of the dumpling-app-processor:

## Deployment

- 1 Cloud Pub/Sub Subscription
- 1 Cloud Dataflow Streaming Job

## Usage

Setup the backend configuration in `backend.tf`:

```hcl
terraform {
  backend "gcs" {
    bucket = "???"
    prefix = "???"
  }
}
```

Add the following variable in `dev.auto.tfvars`:

```hcl
gcp_project                = "???"
service_account_id         = "???"
gcs_bucket                 = "???"
gcp_zone                   = "???"
dataflow_template_location = "???"
dataflow_gcs_location      = "???"
topic_subscription         = "???"
```

Apply the manifest:

```bash
terraform apply
```

