# Dumpling App Service

###### Code example of the blog post: [dumpling-app](../blog_007/)

This module represents the interface and publisher of a kiosk-app. A simple menu built in Svelte
calls a Spring Boot on AppEngine at each click. Finally, a Cloud Pub?sub message is publish to
indicate that a item has been added or removed from the user shopping cart. Find more in the
article: [Explore data contracts with Cloud Pub/Sub](https://blog.loicmdivad.com/posts/2020/04/explore-data-contracts-with-cloud-pub-sub)

## Story

The Dumpling Store has an application to order food in store. The dumpling-app produces records in
Cloud Pub/Sub topic everytime a user click on the interface. This module uses the Pub/Sub Message
Schema feature to enforce the structure of record produced by the app. Doing so the subscribers such
as [dumpling-app-processor](../dumpling-app-processor/) is less likely to face a corrupted message.

## Schemas

Records are serialized in Protobuf so the schema are stored in `*.proto` files.

```protobuf
message PubResponse {
  bool success = 1;
  string message_id = 2;
  string comment = 3;
}
```

A schema can be upload as resource and used when creating a topic:

```bash
gcloud pubsub schemas create <schema_id> \
  --definition=$(cat <path-to-proto>) \
  --type=<schema_type> #AVRO or PROTOCOL_BUFFER
```

This is performed with terraform (see: [terraform sub module](./terraform/)). Then schemas can be
listed and described with gcloud commands:

```bash
gcloud pubsub schemas describe <schema-name>
```

## Usage

1. Create the `.mvn/settings.xml` file containing all the maven parameters

```xml

<profiles>
    <profile>
        <id>default</id>
        <properties>
            <gcp.project>YOUR PROJECT</gcp.project>
            <appengine.port>YOUR PORT</appengine.port>
            <topic.command>YOUR TOPIC1</topic.command>
            <topic.checkout>YOUR TOPIC2</topic.checkout>
        </properties>
    </profile>
</profiles>
```

2. Save the tf variables in `terraform/dev.auto.tfvars` and deploy topics, subscriptions, schemas:

```hcl
gcp_project        = "???"
gcp_zone           = "???"
gcs_bucket         = "???"
service_account_id = "???"
```

```bash
terraform apply
```

3. Build the front end app by passing the AppEngine endpoint

```bash
API_ADDRESS=<app-engine-address> npm build
```

4. Upload the packed application in gcp

```bash
mvn appengine:deploy -s .mvn/settings.xml
```

5. Take the a look at the app

```bash
gcloud app logs tail -s dumpling-app-service
```

## Improvement 