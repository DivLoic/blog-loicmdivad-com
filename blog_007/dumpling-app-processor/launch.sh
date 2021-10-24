mvn -Pdataflow-runner compile exec:java \
    -Dexec.mainClass=fr.ldivad.dumpling.Main \
    -Dexec.args="--project=loicmdivad \
    --gcpTempLocation=gs://ldivad-blog-experiments/dataflow/temp/ \
    --inputTopicSubscription=projects/loicmdivad/subscriptions/test_processor_sub \
    --runner=DataflowRunner \
    --region=europe-west1"