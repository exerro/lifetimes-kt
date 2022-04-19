package me.exerro.lifetimes

/** Thrown when a lifetime which has ended is used by something expecting the
 *  lifetime to be alive. */
class LifetimeHasEnded(
    override val message: String = "Lifetime has ended"
): Exception(message)
