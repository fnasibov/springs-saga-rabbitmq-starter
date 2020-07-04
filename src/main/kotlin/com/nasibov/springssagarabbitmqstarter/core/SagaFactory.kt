package com.nasibov.springssagarabbitmqstarter.core

import com.nasibov.springssagarabbitmqstarter.config.SagaProperties
import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SagaFactory(
        private val sagaProperties: SagaProperties,
        private val amqpAdmin: AmqpAdmin,
        private val rabbitTemplate: RabbitTemplate,
        private val exchange: TopicExchange
) {
    @Value("\${spring.application.name}")
    private val appName: String? = null

    fun <BODY_CLASS: Any> create(
            baseRoutingKey: String,
            body: BODY_CLASS,
            fallbackAction: (BODY_CLASS) -> Unit
    ): Saga<BODY_CLASS> {
        val queue = Queue(sagaProperties.queueName + "_" + appName, true)
        amqpAdmin.declareQueue(queue)
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(baseRoutingKey))
        return Saga(rabbitTemplate, "$baseRoutingKey.action", "$baseRoutingKey.canceled", body, fallbackAction)
    }

}
