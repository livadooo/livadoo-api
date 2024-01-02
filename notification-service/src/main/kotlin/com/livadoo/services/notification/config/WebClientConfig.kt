package com.livadoo.services.notification.config

import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class WebClientConfig(
    private val webClientBuilder: WebClient.Builder,
    private val notificationProperties: NotificationProperties,
) {
    @Bean
    fun zeptoMailWebClient(): WebClient {
        return webClientBuilder
            .baseUrl(notificationProperties.mail.zeptomail.apiUrl)
            .defaultHeaders {
                it.contentType = MediaType.APPLICATION_JSON
                it.accept = listOf(MediaType.APPLICATION_JSON)
                it.set("Authorization", notificationProperties.mail.zeptomail.authorizationHeader)
            }
            .build()
    }
}
