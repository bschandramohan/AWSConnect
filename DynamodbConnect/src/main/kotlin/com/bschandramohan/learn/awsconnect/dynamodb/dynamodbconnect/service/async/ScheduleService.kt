package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.service.async

import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.domain.Schedule
import org.springframework.stereotype.Service

@Service("ScheduleAsyncService")
class ScheduleService(var scheduleRepository: ScheduleRepository) {
    fun get(userId: Long) = scheduleRepository.get(userId)
    fun save(schedule: Schedule) = scheduleRepository.save(schedule)
}
