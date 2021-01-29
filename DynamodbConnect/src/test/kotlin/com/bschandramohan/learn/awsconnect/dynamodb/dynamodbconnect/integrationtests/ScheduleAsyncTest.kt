package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.integrationtests

import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.domain.DaySchedule
import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.domain.Schedule
import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.util.getMonoResult
import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.util.postMonoResult
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.ThreadLocalRandom

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ScheduleAsyncTest {
    private val commonDaySchedules = listOf(DaySchedule("08:00", "15:00", "Mountain View"))
    private val uniqueDaySchedules = listOf(DaySchedule("11:00", "17:00", "Cupertino"))
    private val daySchedules = mapOf(
        "Monday" to commonDaySchedules,
        "Tuesday" to commonDaySchedules,
        "Wednesday" to uniqueDaySchedules,
        "Thursday" to commonDaySchedules,
        "Friday" to commonDaySchedules
    )

    private fun uniqueUserId() = ThreadLocalRandom.current().nextLong(10000, 600000)

    private fun sampleSchedule() = Schedule(uniqueUserId(), daySchedules)

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Test
    fun saveAndGetSchedule_withCorrectInputs_savesSuccessfully() {
        val addSchedule = sampleSchedule()
        val response = postMonoResult("async/schedule/", addSchedule)
        logger.info(response.toString())

        val schedule: Schedule? = getMonoResult("async/schedule/${addSchedule.userId}")
        logger.info(schedule?.toString())
    }
}
