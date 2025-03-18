package io.github.vladyhon.navparams.internal

import io.github.vladyhon.navparams.ConsumableNavParameter
import io.github.vladyhon.navparams.NavParams
import io.github.vladyhon.navparams.internal.queue.NavParameterQueue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

internal class NavParamsImpl : NavParams {
    private val parameters = mutableMapOf<String, NavParameterQueue<Any>>()

    override suspend fun <T> add(name: String, value: T) {
        requireParam(name)
        parameters[name]?.emit(value as Any)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> subscribe(name: String): Flow<ConsumableNavParameter<T>> {
        requireParam(name)
        return parameters[name]!!.values as Flow<ConsumableNavParameter<T>>
    }

    override fun <A : Any, B : Any> subscribe(
        name1: String,
        name2: String,
    ): Flow<Pair<ConsumableNavParameter<A>, ConsumableNavParameter<B>>> {
        requireParam(name1)
        requireParam(name2)

        return combine(subscribe<A>(name1), subscribe<B>(name2)) { a, b -> a to b }
    }

    override fun <A : Any, B : Any, C : Any> subscribe(
        name1: String,
        name2: String,
        name3: String,
    ): Flow<Triple<ConsumableNavParameter<A>, ConsumableNavParameter<B>, ConsumableNavParameter<C>>> {
        requireParam(name1)
        requireParam(name2)
        requireParam(name3)

        return combine(
            subscribe<A>(name1),
            subscribe<B>(name2),
            subscribe<C>(name3),
        ) { a, b, c -> Triple(a, b, c) }
    }

    private fun requireParam(name: String) {
        if (parameters.containsKey(name).not()) {
            parameters[name] = NavParameterQueue.create()
        }
    }
}
