package io.github.vladyhon.navparams.internal.queue

import io.github.vladyhon.navparams.ConsumableNavParameter
import io.github.vladyhon.navparams.internal.consumable.ConsumableNavParameterImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.LinkedList
import java.util.Queue

internal class NavParameterQueueImpl<T : Any> : NavParameterQueue<T> {
    private val mutableValues = MutableSharedFlow<ConsumableNavParameter<T>>(replay = 1)
    private val queue: Queue<T> = LinkedList()
    private val mutex = Mutex()

    override val values: Flow<ConsumableNavParameter<T>> = mutableValues

    override suspend fun emit(value: T) {
        if (mutableValues.replayCache.isNotEmpty()) {
            mutex.withLock { queue.add(value) }
        } else {
            mutableValues.emit(value.toConsumableParameter())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun T.toConsumableParameter(): ConsumableNavParameter<T> =
        ConsumableNavParameterImpl(this) {
            mutex.withLock {
                when (val event = queue.poll()) {
                    null -> mutableValues.resetReplayCache()
                    else -> mutableValues.emit(event.toConsumableParameter())
                }
            }
        }
}
