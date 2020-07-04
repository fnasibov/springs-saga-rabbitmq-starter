package com.nasibov.springssagarabbitmqstarter.core

import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class SagaListener {

    @RabbitListener(queues = ["#{'\${saga.queueName:saga}' + '_' + \${spring.application.name}}"])
    fun onMessage(message: Message) {
        val receivedRoutingKey = message.messageProperties.receivedRoutingKey
        val fallbackAction = SagaFallbackActionHolder.fallbackActionsByFallbackRoutingKey[receivedRoutingKey]

        fallbackAction?.invoke(message)
    }
}
