package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class DynamoDBConnectApplication

fun main(args: Array<String>) {
    runApplication<DynamoDBConnectApplication>(*args)
}
