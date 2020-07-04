package com.nasibov.springssagarabbitmqstarter.config

import com.nasibov.springssagarabbitmqstarter.core.SagaFactory
import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@ConditionalOnBean(RabbitAutoConfiguration::class)
@EnableConfigurationProperties(SagaProperties::class)
class SagaRabbitMqAutoConfiguration(private val sagaProperties: SagaProperties) {

    @Bean
    @ConditionalOnMissingBean
    fun rabbitAdmin(rabbitTemplate: RabbitTemplate): AmqpAdmin {

        return RabbitAdmin(rabbitTemplate)
    }

    @Bean("sagaExchange")
    fun exchange(): TopicExchange {

        return TopicExchange(sagaProperties.topicExchangeName)
    }

    @Bean
    fun sagaFactory(
            @Qualifier("sagaQueue") queue: Queue,
            amqpAdmin: AmqpAdmin,
            rabbitTemplate: RabbitTemplate,
            @Qualifier("sagaExchange") exchange: TopicExchange
    ): SagaFactory {
        return SagaFactory(sagaProperties, amqpAdmin, rabbitTemplate, exchange)
    }

}
