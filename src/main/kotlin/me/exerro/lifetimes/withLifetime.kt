package me.exerro.lifetimes

/** Run [block] with a [Lifetime] context. The lifetime will be ended once
 *  [block] has finished running. */
fun <T> withLifetime(block: context (Lifetime) () -> T): T {
    val lifetime = Lifetime.createDetached()
    val result = with(lifetime, block)
    lifetime.end()
    return result
}

/** Run [block] with a [Lifetime] context. The lifetime will be ended once
 *  [block] has finished running. */
suspend fun <T> withLifetimeSuspend(block: suspend context (Lifetime) () -> T): T {
    val lifetime = Lifetime.createDetached()
    val result = block(lifetime)
    lifetime.end()
    return result
}
