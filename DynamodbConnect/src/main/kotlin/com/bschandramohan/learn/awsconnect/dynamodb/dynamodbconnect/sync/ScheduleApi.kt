package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.sync

import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.domain.Schedule
import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.error.ApiServerError
import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.error.EntityNotFoundError
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("ScheduleSyncApi")
@RequestMapping("/sync/schedule")
class ScheduleApi(var scheduleService: ScheduleService) {
    private val logger = LoggerFactory.getLogger(ScheduleApi::class.java)

    private val entityName = "Schedule"

    @PostMapping("/")
    fun create(@RequestBody schedule: Schedule): ResponseEntity<Any> {
        return try {
            ResponseEntity(scheduleService.save(schedule), HttpStatus.CREATED)
        } catch (e: Exception) {
            logger.error("Error creating schedule", e)
            ApiServerError(entityName, "create", e)
        }
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            val schedule = scheduleService.get(id)
            if (schedule != null) {
                ResponseEntity.ok(schedule)
            } else {
                EntityNotFoundError(entityName)
            }
        } catch (e: Exception) {
            logger.error("Error getting schedule", e)
            ApiServerError(entityName, "get", e)
        }
    }
}
