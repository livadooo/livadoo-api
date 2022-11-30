package com.livadoo.services.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(
                ApiInfoBuilder()
                    .title("Livadoo API Documentation")
                    .description("This documentation exposes the User microservice endpoints information ")
                    .version("1.0.0")
                    .build()
            )
            .enable(true)
            .select()
            .paths(PathSelectors.any())
            .build()
    }
}
