package com.livadoo.services.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import io.mongock.driver.mongodb.reactive.driver.MongoReactiveDriver
import io.mongock.runner.springboot.MongockSpringboot
import io.mongock.runner.springboot.base.MongockInitializingBeanRunner
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MongockConfig(
    private val mongodbProperties: MongodbProperties,
) {

    @Bean
    fun getBuilder(
        reactiveMongoClient: MongoClient,
        context: ApplicationContext,
    ): MongockInitializingBeanRunner {
        return MongockSpringboot.builder()
            .setDriver(MongoReactiveDriver.withDefaultLock(reactiveMongoClient, mongodbProperties.database))
            .addMigrationScanPackage("com.livadoo.services.db.migration")
            .setSpringContext(context)
            .setTransactionEnabled(true)
            .buildInitializingBeanRunner()
    }

    @Bean
    fun mongoClient(): MongoClient {
        val codecRegistry: CodecRegistry = fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()),
        )
        return MongoClients.create(
            MongoClientSettings.builder()
                .applyConnectionString(ConnectionString(mongodbProperties.uri))
                .codecRegistry(codecRegistry)
                .build(),
        )
    }
}
