package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.service.coroutine

import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.domain.Schedule
import org.springframework.stereotype.Service

@Service("ScheduleFlowService")
class ScheduleService(var scheduleRepository: ScheduleRepository) {
    suspend fun get(userId: Long) = scheduleRepository.get(userId)
    suspend fun getAll() = scheduleRepository.getAll()
    suspend fun save(schedule: Schedule) = scheduleRepository.save(schedule)
}
