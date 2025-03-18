package io.github.vladyhon.navparams

public suspend fun <T : Any> ConsumableNavParameter<T>.getAndConsume(): T =
    value.apply { consume() }
