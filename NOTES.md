# Kafka Takeaways

### Binary SPEC

### [Kafka API Versions Request (v4)](https://binspec.org/kafka-api-versions-request-v4)


### [Kafka API Versions Response (v4)](https://binspec.org/kafka-api-versions-Response-v4)

- Message Size      : 4 bytes = 4 + 2 + 1 + 2 + 2 + 4
- Correlation ID    : 4 bytes
- Error Code        : 2 bytes
- APIs count        : 1 bytes = 1+1
- API key           : 2 byte  = 18
- Min Version       : 2 bytes = 0
- Max Version       : 2 bytes = 4

