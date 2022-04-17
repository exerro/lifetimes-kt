package me.exerro.lifetimes

/** Test that [createAlreadyEnded] returns a lifetime which is not alive for
 *  the duration of the test. */
fun testAlreadyEnded() {
    val lifetime = Lifetime.createAlreadyEnded()
    var destructorCalled1 = false
    var destructorCalled2 = false
    var isAliveCalled = false

    assert(!lifetime.isAlive) { "Lifetime was unexpectedly alive" }

    val binding1 = lifetime.onLifetimeEnded { destructorCalled1 = true }
    assert(destructorCalled1) { "Destructor was not called in onLifetimeEnded" }

    val binding2 = lifetime.onLifetimeEndedIfAlive(ifAlive = { isAliveCalled = true })
        { destructorCalled2 = true }
    assert(!destructorCalled2) { "A destructor was called in onLifetimeEndedIfAlive" }
    assert(!isAliveCalled) { "ifAlive callback was called unexpectedly" }

    assert(binding1 == null) { "non-null binding returned from onLifetimeEnded" }
    assert(binding2 == null) { "non-null binding returned from onLifetimeEndedIfAlive" }
}
