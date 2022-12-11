package com.livadoo.services.advice.utils

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.ResponseEntity
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

object AdviceUtils {
    fun setHttpResponse(
        entity: ResponseEntity<Any>,
        exchange: ServerWebExchange,
        mapper: ObjectMapper
    ): Mono<Void> {
        val response = exchange.response
        response.statusCode = entity.statusCode
        response.headers.addAll(entity.headers)
        return try {
            val buffer = response.bufferFactory()
                .wrap(mapper.writeValueAsBytes(entity.body))
            response.writeWith(Mono.just(buffer))
                .doOnError { DataBufferUtils.release(buffer) }
        } catch (ex: JsonProcessingException) {
            Mono.error(ex)
        }
    }
}
