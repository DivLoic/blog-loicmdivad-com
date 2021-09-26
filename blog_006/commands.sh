#!/usr/bin/env bash

ccloud environment create BLOG

ccloud environment use t35556
ccloud kafka cluster create blog-cluster --cloud gcp --region europe-west1

ccloud kafka cluster use lkc-gnv0n
ccloud schema-registry cluster enable --cloud gcp --geo eu

ccloud service-account create "BlogPostAccount" --description "This is a demo service account"

ccloud api-key create --service-account 54876 --resource lkc-gnv0n # kafka cluster id

ccloud api-key create --service-account 54876 --resource lsrc-przmk # registry cluster id

ccloud kafka topic create BLOG-TOPIC
ccloud kafka acl create --allow \
--service-account 54876 \
--operation WRITE \
--topic BLOG-TOPIC