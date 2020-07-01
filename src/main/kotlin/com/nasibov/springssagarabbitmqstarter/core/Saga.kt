package com.nasibov.springssagarabbitmqstarter.core

import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate

class Saga<BODY_CLASS>(
        private val rabbitTemplate: RabbitTemplate,
        queue: Queue,
        private val exchange: TopicExchange,
        private val actionRoutingKey: String,
        fallbackRoutingKey: String,
        private val body: BODY_CLASS,
        private val fallbackAction: (BODY_CLASS) -> Unit
) {

    init {
        val message = rabbitTemplate.receive(queue.name)
        val receivedRoutingKey = message?.messageProperties?.receivedRoutingKey
        if (receivedRoutingKey == fallbackRoutingKey) {
            @Suppress("UNCHECKED_CAST")
            fallbackAction.invoke((rabbitTemplate.messageConverter.fromMessage(message) as BODY_CLASS))
        }
    }
    fun start() {
        rabbitTemplate.convertAndSend(exchange.name, actionRoutingKey)
    }

}
