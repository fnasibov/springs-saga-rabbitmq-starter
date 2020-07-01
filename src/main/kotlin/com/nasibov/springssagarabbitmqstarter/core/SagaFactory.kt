package com.nasibov.springssagarabbitmqstarter.core

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.core.RabbitTemplate

class SagaFactory(
        private val queue: Queue,
        private val amqpAdmin: AmqpAdmin,
        private val rabbitTemplate: RabbitTemplate,
        private val exchange: TopicExchange
) {

    fun <BODY_CLASS> create(
            actionRoutingKey: String,
            body: BODY_CLASS,
            fallbackAction: (BODY_CLASS) -> Unit
    ): Saga<BODY_CLASS> {
        val fallbackRoutingKey = "$actionRoutingKey.cancele"
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(actionRoutingKey))
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(fallbackRoutingKey))
        return Saga(rabbitTemplate,queue,exchange, actionRoutingKey, fallbackRoutingKey, body, fallbackAction)
    }

}
