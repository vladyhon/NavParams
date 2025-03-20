package io.github.vladyhon.navparams.internal

import io.github.vladyhon.navparams.ConsumableNavParameter
import io.github.vladyhon.navparams.NavParams
import io.github.vladyhon.navparams.internal.queue.NavParameterQueue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class NavParamsImpl : NavParams {
    private val dataStore = mutableMapOf<String, NavParameterQueue<Any>>()
    private val mutex = Mutex()

    override suspend fun <T> add(name: String, value: T): Result<Unit> = mutex.withLock {
        runCatching {
            val queue = dataStore.getOrPut(name) { NavParameterQueue() }
            queue.emit(value as Any)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> subscribe(name: String): Flow<ConsumableNavParameter<T>> {
        val queue = dataStore.getOrPut(name) { NavParameterQueue() }
        return queue.values as Flow<ConsumableNavParameter<T>>
    }

    override fun <A : Any, B : Any> subscribe(
        name1: String,
        name2: String,
    ): Flow<Pair<ConsumableNavParameter<A>, ConsumableNavParameter<B>>> =
        combine(
            subscribe<A>(name1),
            subscribe<B>(name2),
        ) { a, b -> a to b }

    override fun <A : Any, B : Any, C : Any> subscribe(
        name1: String,
        name2: String,
        name3: String,
    ): Flow<Triple<ConsumableNavParameter<A>, ConsumableNavParameter<B>, ConsumableNavParameter<C>>> =
        combine(
            subscribe<A>(name1),
            subscribe<B>(name2),
            subscribe<C>(name3),
        ) { a, b, c -> Triple(a, b, c) }
}
