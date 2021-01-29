package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.service.async

import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.aop.TimeIt
import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.domain.Schedule
import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.error.ApiServerError
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("ScheduleAsyncApi")
@RequestMapping("async/schedule")
class ScheduleApi(var scheduleService: ScheduleService) {
    private val logger = LoggerFactory.getLogger(ScheduleApi::class.java)

    @TimeIt
    @PostMapping("/")
    fun create(@RequestBody schedule: Schedule): ResponseEntity<Any> {
        return try {
            ResponseEntity(scheduleService.save(schedule), HttpStatus.CREATED)
        } catch (e: Exception) {
            logger.error("Error creating schedule", e)
            ApiServerError("Schedule", "create", e)
        }
    }

    @TimeIt
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(scheduleService.get(id))
        } catch (e: Exception) {
            logger.error("Error getting schedule", e)
            ApiServerError("Schedule", "get", e)
        }
    }
}
