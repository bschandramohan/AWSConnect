Spring Boot Reactive Data webapplication that demonstrates reactive way of connecting to database (AWS DynamoDB in this case)  

# Pre Run Steps

Before running the spring app, do the following

1. Run `docker-compose up -d` to startup Dynamodb
1. Create the schedule table (We could have used code for generation, but keeping it simple here. This is the common
   practice for production code anyway)

```
aws dynamodb --endpoint-url http://localhost:8000 create-table \
--table-name Schedule \
--attribute-definitions \
   AttributeName=userId,AttributeType=N \
--key-schema \
   AttributeName=userId,KeyType=HASH \
--provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1
```

3. Run the Sync, Async and Flow tests to test the functionality

# Notes

There are three packages : Sync, Async and Flow indicating the three mechanisms

- Sync uses Dynamodb Enhanced client - so responses are obtained synchronously.
- Async uses Dynamodb Enhanced Async client - so responses are obtained in async mode.
- Flow: uses Dynamodb Enhanced Async client - but converts the results to Kotlin flow for consumption

Enhanced library: for directly mapping the results into kotlin objects instead of json response to be parsed manually.
