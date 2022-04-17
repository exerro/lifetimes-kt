package me.exerro.lifetimes

/** A [Lifetime] represents a time span for which it is "alive". Destructors can
 *  be attached to the lifetime to run when the lifetime ends.
 *
 *  @see onLifetimeEnded
 *  @see createDetached
 *  @see createChildOf
 *  @see createNeverEnding
 *  @see createAlreadyEnded
 */
interface Lifetime {
    /** Whether this lifetime is currently alive. */
    val isAlive: Boolean

    /** If the lifetime is alive, attach [destructor] to the lifetime's list of
     *  destructors. Otherwise, run [destructor] immediately.
     *  [destructor] will be called either immediately, or when the lifetime
     *  is ended.
     *  @return A [LifetimeBinding] if the destructor was deferred.
     *  @see onLifetimeEndedIfAlive
     */
    fun onLifetimeEnded(destructor: () -> Unit): LifetimeBinding?

    /** If the lifetime is alive, attach [destructor] to the lifetime's list of
     *  destructors, call [ifAlive], and return a [LifetimeBinding]. Otherwise,
     *  return null. [ifAlive] will be called inside a synchronised block,
     *  meaning the lifetime will remain alive for the entirety of the call to
     *  [ifAlive]. If the lifetime is not alive when this function is called,
     *  [destructor] will *not* be called.
     *
     *  This function is useful over [onLifetimeEnded] when special action
     *  should be taken beyond attaching a destructor, for example with more
     *  complex relationships between resources. One can also safely avoid
     *  resource allocation with ended lifetimes using this.
     *
     *  @param ifAlive Called if the lifetime was alive when
     *                 [onLifetimeEndedIfAlive] was called.
     *  @param destructor Added to the lifetime's list of destructors if the
     *                    lifetime was alive when [onLifetimeEndedIfAlive] was
     *                    called. Never called directly by this function.
     *  @return A [LifetimeBinding] if the lifetime was alive when
     *          [onLifetimeEndedIfAlive] was called.
     *  @see onLifetimeEnded
     */
    fun onLifetimeEndedIfAlive(
        ifAlive: () -> Unit = {},
        destructor: () -> Unit,
    ): LifetimeBinding?

    /** A [Managed] lifetime is a [Lifetime] that can be explicitly ended by
     *  calling [end].
     *  @see createDetached
     */
    interface Managed: Lifetime {
        /** End this lifetime. */
        fun end()
    }

    /** @see Lifetime */
    companion object
}
