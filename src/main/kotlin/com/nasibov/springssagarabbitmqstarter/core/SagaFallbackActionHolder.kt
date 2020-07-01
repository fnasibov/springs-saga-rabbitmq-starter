package com.nasibov.springssagarabbitmqstarter.core

object SagaFallbackActionHolder {
    val bodyClassByFallbackRoutingKey: Map<String, Class<Any>> = mutableMapOf()
    val fallbackActionsByBodyClass: Map<Class<Any>, (Any) -> Unit> = mutableMapOf()
}
