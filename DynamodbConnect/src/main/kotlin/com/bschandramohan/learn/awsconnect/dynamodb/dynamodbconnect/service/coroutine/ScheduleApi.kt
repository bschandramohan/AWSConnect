package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.service.coroutine

import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.aop.TimeIt
import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.domain.Schedule
import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.error.ApiServerError
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * API using coroutine flows.
 *
 * Reference:
 * https://spring.io/blog/2019/04/12/going-reactive-with-spring-coroutines-and-kotlin-flow
 * https://elizarov.medium.com/futures-cancellation-and-coroutines-b5ce9c3ede3a
 */
@RestController("ScheduleFlowApi")
@RequestMapping("/flow/schedule")
class ScheduleApi(var scheduleService: ScheduleService) {
    private val logger = LoggerFactory.getLogger(ScheduleApi::class.java)

    private val entityName = "Schedule"

    @TimeIt
    @PostMapping("/")
    suspend fun create(@RequestBody schedule: Schedule): ResponseEntity<Any> {
        return try {
            ResponseEntity(scheduleService.save(schedule), HttpStatus.CREATED)
        } catch (e: Exception) {
            logger.error("Error creating schedule", e)
            ApiServerError(entityName, "create", e)
        }
    }

    @TimeIt
    @GetMapping("/{id}")
    suspend fun get(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            val schedule = scheduleService.get(id)
            ResponseEntity.ok(schedule)
        } catch (e: Exception) {
            logger.error("Error getting schedule", e)
            ApiServerError(entityName, "get", e)
        }
    }
}
