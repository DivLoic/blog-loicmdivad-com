# Dumpling App Service

###### Terraform manifest of the dumpling-app-service:

## Deployment

- 1 Cloud AppEngine
- 2 Cloud Pub/Sub Topic
- 2 Cloud Pub/Sub Subscription
- 2 Cloud Pub/Sub Message Schema

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
gcp_project        = "???"
gcp_zone           = "???"
gcs_bucket         = "???"
service_account_id = "???"
```

Apply the manifest:

```bash
terraform apply
```

