package me.exerro.lifetimes

/** A [LifetimeBinding] represents a destructor that has been attached to a
 *  [Lifetime], allowing that destructor to be removed from the lifetime.
 *  @see disconnect
 */
fun interface LifetimeBinding {
    /** Disconnect the associated destructor from the associated lifetime so
     *  that the destructor will not be called when the lifetime ends.
     *  Note, this interface makes no attempt to assert or report whether the
     *  destructor has not already been called - that is up to the user to
     *  manage.
     *  @see Lifetime
     *  @see Lifetime.onLifetimeEnded
     *  @see Lifetime.onLifetimeEndedIfAlive
     */
    fun disconnect()

    /** @see LifetimeBinding */
    companion object
}
