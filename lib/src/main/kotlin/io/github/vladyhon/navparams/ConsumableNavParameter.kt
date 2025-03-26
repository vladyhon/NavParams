package io.github.vladyhon.navparams

import io.github.vladyhon.navparams.internal.consumable.ConsumableNavParameterImpl

public fun <T : Any> ConsumableNavParameter(
    value: T,
    consumed: suspend () -> Unit,
): ConsumableNavParameter<T> = ConsumableNavParameterImpl(value, consumed)

public interface ConsumableNavParameter<T : Any> {
    public val value: T
    public suspend fun consume()
}

public suspend fun <T : Any> ConsumableNavParameter<T>.getAndConsume(): T =
    value.apply { consume() }

public fun <T : Any> T.asConsumableNavParameter(
    consumed: suspend () -> Unit,
): ConsumableNavParameter<T> = ConsumableNavParameterImpl(this, consumed)
