package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.service.sync

import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.aop.TimeIt
import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.domain.Schedule
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@Repository("ScheduleSyncRepository")
class ScheduleRepository(dynamodbSyncClient: DynamoDbEnhancedClient) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val table = dynamodbSyncClient.table("Schedule", tableSchema)

    @TimeIt
    fun save(schedule: Schedule) {
        table.putItem(schedule)
        logger.info("Saved schedule for user=${schedule.userId}")
    }

    @TimeIt
    fun get(userId: Long): Schedule? {
        val key = Key.builder().partitionValue(userId).build()
        return table.getItem(key)
    }

//    fun getAll(): Flux<Schedule> {
//        val itemPage = table.scan()
//        itemPage.
//    }

    companion object {
        private val tableSchema = TableSchema.fromBean(Schedule::class.java)
    }
}
