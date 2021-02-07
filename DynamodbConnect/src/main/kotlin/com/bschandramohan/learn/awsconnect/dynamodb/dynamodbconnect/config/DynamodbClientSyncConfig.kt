package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI

@Configuration
class DynamodbClientSyncConfig(val dynamoConfigProperties: DynamoConfigProperties) {
    @Bean
    fun dynamoDbSyncClient(): DynamoDbClient = DynamoDbClient.builder()
        .region(Region.of(dynamoConfigProperties.region))
        .endpointOverride(URI.create(dynamoConfigProperties.endpoint))
        .credentialsProvider(DefaultCredentialsProvider.builder().build())
        .build()

    @Bean
    fun dynamoDbEnhancedSyncClient(): DynamoDbEnhancedClient = DynamoDbEnhancedClient
        .builder()
        .dynamoDbClient(dynamoDbSyncClient())
        .build()
}
