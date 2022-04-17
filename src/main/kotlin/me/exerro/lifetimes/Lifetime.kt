package me.exerro.lifetimes

/** TODO */
interface Lifetime {
    /** Whether this lifetime is currently alive. */
    val isAlive: Boolean

    /** If the lifetime is alive, attach [destructor] to the lifetime's list of
     *  destructors. Otherwise, run [destructor] immediately.
     *  [destructor] will be called either immediately, or when the lifetime
     *  is ended.
     *  @return A [LifetimeBinding] if the destructor was deferred.
     *  */
    fun onLifetimeEnded(destructor: () -> Unit): LifetimeBinding?

    /** If the lifetime is alive, attach [destructor] to the lifetime's list of
     *  destructors, call [ifAlive], and return a [LifetimeBinding]. Otherwise,
     *  return null. [ifAlive] will be called inside a synchronised block,
     *  meaning the lifetime will not be ended for the duration of the call to
     *  [ifAlive]. If the lifetime is not alive when this function is called,
     *  [destructor] will *not* be called.
     *  @param ifAlive Called if the lifetime was alive when
     *                 [onLifetimeEndedIfAlive] was called.
     *  @param destructor Added to the lifetime's list of destructors if the
     *                    lifetime was alive when [onLifetimeEndedIfAlive] was
     *                    called. Never called directly by this function.
     *  @return A [LifetimeBinding] if the lifetime was alive when
     *          [onLifetimeEndedIfAlive] was called.
     *  */
    fun onLifetimeEndedIfAlive(
        ifAlive: () -> Unit = {},
        destructor: () -> Unit,
    ): LifetimeBinding?

    /** TODO */
    interface Managed: Lifetime {
        /** End this lifetime. */
        fun end()
    }

    /** @see Lifetime */
    companion object
}
