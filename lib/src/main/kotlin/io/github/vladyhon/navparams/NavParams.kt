package io.github.vladyhon.navparams

import io.github.vladyhon.navparams.internal.NavParamsImpl
import kotlinx.coroutines.flow.Flow

public interface NavParams {
    public suspend fun <T> add(name: String, value: T)
    public fun <A : Any> subscribe(name: String): Flow<ConsumableNavParameter<A>>
    public fun <A : Any, B : Any> subscribe(
        name1: String,
        name2: String,
    ): Flow<Pair<ConsumableNavParameter<A>, ConsumableNavParameter<B>>>

    public fun <A : Any, B : Any, C : Any> subscribe(
        name1: String,
        name2: String,
        name3: String,
    ): Flow<Triple<ConsumableNavParameter<A>, ConsumableNavParameter<B>, ConsumableNavParameter<C>>>

    public companion object {
        public fun create(): NavParams = NavParamsImpl()
    }
}
