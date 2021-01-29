package com.bschandramohan.learn.awsconnect.dynamodb.dynamodbconnect.util

import kotlinx.coroutines.flow.firstOrNull
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow
import reactor.core.publisher.Mono
import java.time.Duration

val client = WebClient.create("http://localhost:8080/")

class TestWebClient

private val logger = LoggerFactory.getLogger(TestWebClient::class.java)

inline fun <reified T> getMonoResult(uriParam: String): T? = client
    .get()
    .uri(uriParam)
    .retrieve()
    .onStatus(HttpStatus::isError, ::renderErrorResponse)
    .bodyToMono(T::class.java)
    .block(Duration.ofSeconds(1))

inline fun <reified T> getFluxResult(uriParam: String): MutableList<T>? = client
    .get()
    .uri(uriParam)
    .retrieve()
    .onStatus(HttpStatus::isError, ::renderErrorResponse)
    .bodyToFlux(T::class.java)
    .collectList()      // Ref: https://www.programcreek.com/java-api-examples/?api=org.springframework.web.reactive.function.client.WebClient
    .block(Duration.ofSeconds(1))

inline fun <reified T> postMonoResult(uriParam: String, dataToPost: T): T? = client
    .post()
    .uri(uriParam)
    .body(BodyInserters.fromValue(dataToPost))
    .retrieve()
    .onStatus(HttpStatus::isError, ::renderErrorResponse)
    .bodyToMono(T::class.java)
    .block(Duration.ofSeconds(1))


/* Note:
    Without <T: Any> here we get error:
        Type argument is not within its bounds.
        Expected: Any
        Found: T
        Solution: add type : Any ; Ref: https://stackoverflow.com/a/63535433/207552

    Without reified, we get the error: "Cannot use 'T' as reified type parameter. Use a class instead."
    https://kotlinexpertise.com/kotlin-reified-types/

 */
suspend inline fun <reified T: Any> postFlowResult(uriParam: String, dataToPost: T): T? = client
    .post()
    .uri(uriParam)
    .body(BodyInserters.fromValue(dataToPost))
    .retrieve()
    .onStatus(HttpStatus::isError, ::renderErrorResponse)
    .bodyToFlow<T>()
    .firstOrNull()


suspend inline fun <reified T: Any> getFlowResult(uriParam: String): T? = client
    .get()
    .uri(uriParam)
    .retrieve()
    .onStatus(HttpStatus::isError, ::renderErrorResponse)
    .bodyToFlow<T>()
    .firstOrNull()

fun renderErrorResponse(clientResponse: ClientResponse): Mono<Throwable> {
    logger.error("Error on call. StatusCode=${clientResponse.statusCode()}")

    if (clientResponse.statusCode().is3xxRedirection) {
        return Mono.error(Exception("Redirection called for ${clientResponse.headers().header("Location")[0]}"))
    } else if (clientResponse.statusCode().is4xxClientError) {
        return Mono.error(Exception("Invalid client call. Status=${clientResponse.statusCode()}"))
    }
    return Mono.error(Exception("Server error. Status=${clientResponse.statusCode()}"))
}
