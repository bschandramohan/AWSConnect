package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.domain

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
data class Schedule(
    @get:DynamoDbPartitionKey
    var userId: Long = 0,
    var weeklySchedule: Map<String, List<DaySchedule>> = mapOf()
)

@DynamoDbBean
data class DaySchedule(
    var startTime: String = "",
    var endTime: String = "",
    var officeLocation: String = ""
)

// Using val gives: Attempt to execute an operation that requires a primary index without defining any primary key attributes in the table metadata.