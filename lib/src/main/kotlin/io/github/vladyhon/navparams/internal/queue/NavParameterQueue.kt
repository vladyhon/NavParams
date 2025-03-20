package io.github.vladyhon.navparams.internal.queue

import kotlinx.coroutines.flow.FlowCollector

internal fun <T : Any> NavParameterQueue(): NavParameterQueue<T> = NavParameterQueueImpl()

internal interface NavParameterQueue<T : Any> : FlowCollector<T>, NavParameterSource<T>
