package com.nasibov.springssagarabbitmqstarter.core

import org.springframework.amqp.core.Message

object SagaFallbackActionHolder {
    val fallbackActionsByFallbackRoutingKey: MutableMap<String, (Message) -> Unit> = mutableMapOf()
}
