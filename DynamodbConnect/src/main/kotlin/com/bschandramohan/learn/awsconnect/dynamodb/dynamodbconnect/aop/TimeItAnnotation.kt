package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import kotlin.system.measureTimeMillis

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TimeIt

@Aspect
@Configuration
class TimeItAnnotationAspect {
    var logger: Logger = LoggerFactory.getLogger(TimeItAnnotationAspect::class.java)

    @Around("@annotation(com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.aop.TimeIt)")
    @Throws(Throwable::class)
    fun calculateTimeTaken(proceedingJoinPoint: ProceedingJoinPoint): Any? {
        val arguments = StringBuilder()
        proceedingJoinPoint.args.forEach { arguments.append(it).append("\n") }
        logger.info("Started execution of $proceedingJoinPoint with arguments: $arguments")
        var retValue: Any? = null
        val time = measureTimeMillis {
            try {
                retValue = proceedingJoinPoint.proceed()
            } catch (e: Exception) { // ignore
            }
        }
        //logger.info("Method $proceedingJoinPoint completed execution in $time ms")
        logger.info("Invoked Method=${proceedingJoinPoint.signature.declaringTypeName}.${proceedingJoinPoint.signature.name} timeTaken=$time ms")
        return retValue
    }
}
