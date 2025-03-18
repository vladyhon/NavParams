package io.github.vladyhon.navparams.internal.consumable

import io.github.vladyhon.navparams.ConsumableNavParameter
import java.util.concurrent.atomic.AtomicBoolean

internal class ConsumableNavParameterImpl<T : Any>(
    override val value: T,
    private val consumed: suspend () -> Unit,
) : ConsumableNavParameter<T> {
    private val wasConsumed = AtomicBoolean()

    override suspend fun consume() {
        if (wasConsumed.compareAndSet(false, true)) {
            consumed()
        }
    }
}
