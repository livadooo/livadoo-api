package com.livadoo.utils.spring

import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange

suspend inline fun <reified T : Any> ClientResponse.parseResponse(): T =
    if (this.statusCode().is2xxSuccessful) {
        this.awaitBody()
    } else {
        throw this.awaitBody<RuntimeException>()
    }

suspend inline fun <reified T : Any> WebClient.RequestHeadersSpec<out WebClient.RequestHeadersSpec<*>>.awaitAndParseResponse(): T =
    this.awaitExchange { it.parseResponse() }
