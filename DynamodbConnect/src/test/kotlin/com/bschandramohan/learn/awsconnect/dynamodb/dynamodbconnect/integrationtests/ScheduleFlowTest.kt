package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.integrationtests

import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.domain.DaySchedule
import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.domain.Schedule
import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.util.getFlowResult
import com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.util.postFlowResult
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.ThreadLocalRandom

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ScheduleFlowTest {
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

    @Test
    fun saveAndGetSchedule_withCorrectInputs_savesSuccessfully() = runBlocking {
        val addSchedule = sampleSchedule()
        val response = postFlowResult("async/schedule/", addSchedule)
        require(response == null) { "Saving schedule shouldn't return an object"}

        val schedule = getFlowResult<Schedule>("async/schedule/${addSchedule.userId}")
        // require is better than assert here, as this makes sure subsequent calls need not be done with schedule?.userId etc.
        require(schedule != null)
        assert(schedule.userId == addSchedule.userId)
        assert(schedule.weeklySchedule.isNotEmpty())
    }

    @Test
    fun getSchedule_withNotCreated_returnsNull() = runBlocking {
        val response = getFlowResult<Schedule>("flow/schedule/1")
        assert(response == null) { "Schedule should not be available for user"}
    }
}
