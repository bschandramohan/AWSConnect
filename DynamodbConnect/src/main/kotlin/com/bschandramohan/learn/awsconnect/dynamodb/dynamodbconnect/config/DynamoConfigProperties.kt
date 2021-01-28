package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "application.dynamodb")
data class DynamoConfigProperties(
    val region: String,
    val endpoint: String
)