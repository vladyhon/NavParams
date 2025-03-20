package io.github.vladyhon.navparams

public interface ConsumableNavParameter<T : Any> {
    public val value: T
    public suspend fun consume()
}

public suspend fun <T : Any> ConsumableNavParameter<T>.getAndConsume(): T =
    value.apply { consume() }
