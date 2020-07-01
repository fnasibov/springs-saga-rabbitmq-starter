package com.nasibov.springssagarabbitmqstarter.core

import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class SagaListener(private val rabbitTemplate: RabbitTemplate) {

    @RabbitListener(queues = ["#{'\${saga.queueName:saga}' + '_' + \${spring.application.name}}"])
    fun onMessage(message: Message) {
        val receivedRoutingKey = message.messageProperties.receivedRoutingKey
        val fallbackAction = SagaFallbackActionHolder.fallbackActionsByFallbackRoutingKey[receivedRoutingKey]

        fallbackAction?.invoke(message)
    }
}
