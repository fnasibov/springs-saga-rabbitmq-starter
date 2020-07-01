package com.nasibov.springssagarabbitmqstarter.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "saga")
class SagaProperties {
    val topicExchangeName = "saga_exchange"
    val queueName = "saga"
}
