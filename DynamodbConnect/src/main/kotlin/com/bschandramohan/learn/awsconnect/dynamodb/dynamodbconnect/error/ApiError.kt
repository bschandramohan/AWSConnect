package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class ApiServerError(val entity: String, val operation: String, val e: Exception) : ResponseEntity<Any>(
    "Server error performing op=$operation on entity=$entity; exceptionMessage=${e.message}",
    HttpStatus.INTERNAL_SERVER_ERROR
)

data class EntityNotFoundError(val entity: String) : ResponseEntity<Any>(
    "Entity=$entity NOT found",
    HttpStatus.NOT_FOUND
)
