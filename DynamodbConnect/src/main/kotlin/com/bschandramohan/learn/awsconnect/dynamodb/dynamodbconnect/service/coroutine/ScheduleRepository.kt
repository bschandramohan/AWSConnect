package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.service.coroutine

import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.aop.TimeIt
import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.domain.Schedule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.reactive.asFlow
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher

@Repository("ScheduleFlowRepository")
class ScheduleRepository(dynamoClient: DynamoDbEnhancedAsyncClient) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val table = dynamoClient.table("Schedule", tableSchema)

    @TimeIt
    suspend fun save(schedule: Schedule): Flow<Void> {
        val itemFuture = table.putItem(schedule)

        return flow {
            emit(itemFuture.get())
        }
    }

    @TimeIt
    suspend fun get(userId: Long): Flow<Schedule> {
        val key = Key.builder().partitionValue(userId).build()
        val itemFuture = table.getItem(key)
        return flow {
            emit(itemFuture.get())
        }

        // Code to print and return data
//        val flowToReturn: Flow<Schedule> = flow {
//            // This block is only called when flow is consumed via collect
//            // logger.info("Get::ItemFuture invoked now")
//            emit(itemFuture.get())
//        }
//
//        flowToReturn.collect { item -> logger.info("item=$item") }
//
//        return flowToReturn
    }

    @TimeIt
    suspend fun getAll(): Flow<Schedule> {
        val scanResults: PagePublisher<Schedule> = table.scan()
        // Test code to print the items
        // scanResults.items().subscribe { item -> logger.info("Inside repository; $item") }

        return scanResults.items().asFlow()
    }

    companion object {
        private val tableSchema = TableSchema.fromBean(Schedule::class.java)
    }
}
