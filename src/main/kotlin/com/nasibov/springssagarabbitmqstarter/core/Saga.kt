package com.nasibov.springssagarabbitmqstarter.core

import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate

@Suppress("UNCHECKED_CAST")
class Saga<BODY_CLASS: Any>(
        private val rabbitTemplate: RabbitTemplate,
        queue: Queue,
        private val exchange: TopicExchange,
        private val actionRoutingKey: String,
        fallbackRoutingKey: String,
        private val body: BODY_CLASS,
        private val fallbackAction: (BODY_CLASS) -> Unit
) {

   init {
       SagaFallbackActionHolder.fallbackActionsByFallbackRoutingKey[fallbackRoutingKey] = {
           val fallbackBody = rabbitTemplate.messageConverter.fromMessage(it) as BODY_CLASS
           fallbackAction.invoke(fallbackBody)
       }
   }
    fun start() {
        rabbitTemplate.convertAndSend(actionRoutingKey, body)
    }
}
