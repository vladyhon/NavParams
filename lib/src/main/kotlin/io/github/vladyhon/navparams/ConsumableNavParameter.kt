package io.github.vladyhon.navparams

public interface ConsumableNavParameter<T : Any> {
    public val value: T
    public suspend fun consume()
}
