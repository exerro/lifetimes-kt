package me.exerro.lifetimes

import java.util.concurrent.atomic.AtomicInteger

/** Create a detached lifetime which can be ended explicitly.
 *
 *  Care should be taken when using this function as losing the reference to the
 *  [Managed][Lifetime.Managed] instance will result in the lifetime never
 *  ending.
 *  @see createChildOf
 *  @see createNeverEnding
 *  @see createAlreadyEnded
 */
fun Lifetime.Companion.createDetached() = object: Lifetime.Managed {
    private val destructors = mutableListOf<() -> Unit>()
    private val lock = Any()

    override var isAlive = true

    override fun <T> keepLifetimeAliveInOrElse(block: () -> T, orElse: () -> T) =
        when (val v = synchronized(lock) {
            if (isAlive) Optional.Of(block())
            else Optional.Empty
        }) {
            is Optional.Of -> v.value
            Optional.Empty -> orElse()
        }

    override fun end() = synchronized(lock) {
        if (isAlive) {
            isAlive = false
            destructors.toList()
        }
        else emptyList()
    } .asReversed().forEach { it() }

    override fun onLifetimeEnded(destructor: () -> Unit) = synchronized(lock) {
        val binding = onLifetimeEndedIfAlive(destructor = destructor)
        if (binding == null) destructor()
        binding
    }

    override fun onLifetimeEndedIfAlive(ifAlive: () -> Unit, destructor: () -> Unit) = synchronized(lock) {
        if (isAlive) {
            ifAlive()
            destructors.add(destructor)
            LifetimeBinding {
                synchronized(lock) { destructors.remove(destructor) }
            }
        }
        else null
    }
}

/** Create a lifetime which is a "child" of [lifetimes]. If [all] is `false`,
 *  the resultant lifetime will end when *any* of the parent [lifetimes] end.
 *  If [all] is `true`, the resultant lifetime will end only when *all* of the
 *  parent [lifetimes] end.
 *  @throws IllegalArgumentException if [lifetimes] is empty.
 *  @see createDetached
 */
fun Lifetime.Companion.createChildOf(
    lifetimes: Collection<Lifetime>,
    all: Boolean = false,
): Lifetime {
    if (lifetimes.isEmpty())
        throw IllegalArgumentException("no lifetimes were given")

    val lifetime = createDetached()
    val counter = AtomicInteger(if (all) lifetimes.size else 1)

    lifetimes.forEach { it.onLifetimeEnded {
        if (counter.decrementAndGet() == 0) lifetime.end()
    } }

    return lifetime
}

/** Create a lifetime which is a "child" of [lifetimes]. If [all] is `false`,
 *  the resultant lifetime will end when *any* of the parent [lifetimes] end.
 *  If [all] is `true`, the resultant lifetime will end only when *all* of the
 *  parent [lifetimes] end.
 *  @throws IllegalArgumentException if [lifetimes] is empty.
 *  @see createDetached
 */
fun Lifetime.Companion.createChildOf(
    vararg lifetimes: Lifetime,
    all: Boolean = false,
) = createChildOf(lifetimes.toList(), all = all)

/** Create a lifetime which will never end. [isAlive][Lifetime.isAlive] will
 *  always be `true` for the resultant object, and destructors bound using
 *  [onLifetimeEndedIfAlive][Lifetime.onLifetimeEndedIfAlive] and
 *  [onLifetimeEnded][Lifetime.onLifetimeEnded] will be ignored with no action
 *  taken.
 *  @see createAlreadyEnded
 */
fun Lifetime.Companion.createNeverEnding() = object: Lifetime {
    override val isAlive = true

    override fun <T> keepLifetimeAliveInOrElse(block: () -> T, orElse: () -> T) =
        block()

    override fun onLifetimeEnded(destructor: () -> Unit) =
        LifetimeBinding { /* do nothing */ }

    override fun onLifetimeEndedIfAlive(ifAlive: () -> Unit, destructor: () -> Unit) =
        run { ifAlive(); LifetimeBinding { /* do nothing */ } }
}

/** Create a lifetime which has already ended. [isAlive][Lifetime.isAlive] will
 *  always be `false` for the resultant object, and destructors bound using
 *  [onLifetimeEnded][Lifetime.onLifetimeEnded] will always be called
 *  immediately, whilst
 *  [onLifetimeEndedIfAlive][Lifetime.onLifetimeEndedIfAlive] will never take
 *  any action.
 *  @see createNeverEnding
 */
fun Lifetime.Companion.createAlreadyEnded() = object: Lifetime {
    override val isAlive = false

    override fun <T> keepLifetimeAliveInOrElse(block: () -> T, orElse: () -> T) =
        orElse()

    override fun onLifetimeEnded(destructor: () -> Unit): LifetimeBinding? {
        destructor()
        return null
    }

    override fun onLifetimeEndedIfAlive(ifAlive: () -> Unit, destructor: () -> Unit): LifetimeBinding? =
        null
}

private sealed interface Optional<out T> {
    data class Of<T>(val value: T): Optional<T>
    object Empty: Optional<Nothing>
}
