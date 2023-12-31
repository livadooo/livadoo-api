package com.livadoo.services.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class ClockConfig {
    @Bean
    fun getClock(): Clock = Clock.systemUTC()
}
