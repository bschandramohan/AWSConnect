package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.async

import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.domain.Schedule
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@Repository("ScheduleAsyncRepository")
class ScheduleRepository(dynamoClient: DynamoDbEnhancedAsyncClient) {

    private val logger = LoggerFactory.getLogger(ScheduleRepository::class.java)

    private val table = dynamoClient.table("Schedule", tableSchema)

    fun save(schedule: Schedule): Mono<Unit> {
        val itemFuture = table.putItem(schedule)

        return Mono.fromFuture(itemFuture)
            .map { }
            .doOnError { logger.error("Exception saving schedule - $it") }
    }

    fun get(userId: Long): Mono<Schedule> {
        val key = Key.builder().partitionValue(userId).build()
        val itemFuture = table.getItem(key)
        return Mono.fromFuture(itemFuture)
            .doOnError { logger.error("Exception fetching schedule for user=$userId - $it") }
    }
//
//    fun getAll(): Flux<Schedule> {
//        val itemPage = table.scan()
//        itemPage.
//    }

    companion object {
        private val tableSchema = TableSchema.fromBean(Schedule::class.java)
    }
}
