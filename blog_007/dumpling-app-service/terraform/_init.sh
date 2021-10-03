#!/usr/bin/env bash

random() {
  cat /dev/urandom | LC_ALL=C tr -dc 'a-z0-9' | fold -w 6 | head -n 1
}

APP="${APP:-dumpling-app}"
GCP_PROJECT="${GCP_PROJECT:-loicmdivad}"
SERVICE_ACCOUNT_ROOT="${SERVICE_ACCOUNT_ROOT:-delicious-sa}"
SERVICE_ACCOUNT="${SERVICE_ACCOUNT:-${SERVICE_ACCOUNT_ROOT}-$(random)}"

gcloud iam service-accounts create "${SERVICE_ACCOUNT}" \
  --project="${GCP_PROJECT}" \
  --description="SA used to complete the blog post demo on Pub/Sub data contract" \
  --display-name="Dumpling App Project Service Account"

gcloud projects add-iam-policy-binding "${GCP_PROJECT}" \
    --member="serviceAccount:${SERVICE_ACCOUNT}@${GCP_PROJECT}.iam.gserviceaccount.com" \
    --role="roles/compute.admin"

gcloud iam service-accounts add-iam-policy-binding \
  ${SERVICE_ACCOUNT}@loicmdivad.iam.gserviceaccount.com \
  --member="user:${EMAIL}" \
  --role="roles/iam.serviceAccountTokenCreator"

gcloud iam service-accounts add-iam-policy-binding \
    "${COMPUTE_ACCOUNT}"-compute@developer.gserviceaccount.com \
    --member="serviceAccount:${SERVICE_ACCOUNT}@${GCP_PROJECT}.iam.gserviceaccount.com" \
    --role="roles/iam.serviceAccountUser"