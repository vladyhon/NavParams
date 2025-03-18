package io.github.vladyhon.navparams.internal.queue

import io.github.vladyhon.navparams.ConsumableNavParameter
import kotlinx.coroutines.flow.Flow

internal interface NavParameterSource<T : Any> {
    val values: Flow<ConsumableNavParameter<T>>
}
