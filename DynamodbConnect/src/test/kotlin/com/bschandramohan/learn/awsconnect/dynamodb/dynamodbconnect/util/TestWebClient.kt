package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.util

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.Duration

private val client = WebClient.create("http://localhost:8080/")

class TestWebClient

private val logger = LoggerFactory.getLogger(TestWebClient::class.java)

fun <T> getMonoResult(uriParam: String, clazz: Class<T>): T? = client
    .get()
    .uri(uriParam)
    .retrieve()
    .onStatus(HttpStatus::isError, ::renderErrorResponse)
    .bodyToMono(clazz)
    .block(Duration.ofSeconds(1))

fun <T> getFluxResult(uriParam: String, clazz: Class<T>): MutableList<T>? = client
    .get()
    .uri(uriParam)
    .retrieve()
    .onStatus(HttpStatus::isError, ::renderErrorResponse)
    .bodyToFlux(clazz)
    .collectList()      // Ref: https://www.programcreek.com/java-api-examples/?api=org.springframework.web.reactive.function.client.WebClient
    .block(Duration.ofSeconds(1))

fun <T> postMonoResult(uriParam: String, dataToPost: T, clazz: Class<T>): T? = client
    .post()
    .uri(uriParam)
    .body(BodyInserters.fromValue(dataToPost))
    .retrieve()
    .onStatus(HttpStatus::isError, ::renderErrorResponse)
    .bodyToMono(clazz)
    .block(Duration.ofSeconds(1))

fun renderErrorResponse(clientResponse: ClientResponse): Mono<Throwable> {
    logger.error("Error on call. StatusCode=${clientResponse.statusCode()}")

    if (clientResponse.statusCode().is3xxRedirection) {
        return Mono.error(Exception("Redirection called for ${clientResponse.headers().header("Location")[0]}"))
    } else if (clientResponse.statusCode().is4xxClientError) {
        return Mono.error(Exception("Invalid client call. Status=${clientResponse.statusCode()}"))
    }
    return Mono.error(Exception("Server error. Status=${clientResponse.statusCode()}"))
}
