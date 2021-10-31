# Dumpling App

###### Code example of the blog post: dumpling-app

See the blog post
at:  [Explore data contracts with Cloud Pub/Sub](https://blog.loicmdivad.com/posts/2020/04/explore-data-contracts-with-cloud-pub-sub)

## Story

The Dumpling Store has an application to order food on site. This OMS is event oriented and publish
record message to Cloud Pub/sub. The blog post shares the story of how this team enforces schema
with the nes Pub/Sub Message Schema feature. This module aggregates different part of the story.

## Sub Modules

- [X] [dumpling-app-service](./dumpling-app-service/)
- [X] [dumpling-app-processor](./dumpling-app-processor/)
- [X] [pubsub-schema-maven-plugin](./pubsub-schema-maven-plugin/)
- [X] [dumpling-cli](./dumpling-cli/) (demo only)


