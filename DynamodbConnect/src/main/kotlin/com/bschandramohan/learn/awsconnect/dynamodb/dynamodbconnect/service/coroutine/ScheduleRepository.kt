package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.service.coroutine

import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.aop.TimeIt
import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.domain.Schedule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@Repository("ScheduleFlowRepository")
class ScheduleRepository(dynamoClient: DynamoDbEnhancedAsyncClient) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val table = dynamoClient.table("Schedule", tableSchema)

    @TimeIt
    suspend fun save(schedule: Schedule): Flow<Unit> {
        val itemFuture = table.putItem(schedule)

        val monoResponse = Mono.fromFuture(itemFuture)
            .map { }
            .doOnError { logger.error("Exception saving schedule for user=${schedule.userId}. Error=$it") }

        return monoResponse.asFlow()
    }

    @TimeIt
    suspend fun get(userId: Long): Flow<Schedule> {
        val key = Key.builder().partitionValue(userId).build()
        val itemFuture = table.getItem(key)

        val scheduleMono = Mono.fromFuture(itemFuture)
            .doOnError { logger.error("Exception fetching schedule for user=$userId Error=$it") }
        return scheduleMono.asFlow()
    }

    companion object {
        private val tableSchema = TableSchema.fromBean(Schedule::class.java)
    }
}
