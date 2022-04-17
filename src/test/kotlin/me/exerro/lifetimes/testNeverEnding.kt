package me.exerro.lifetimes

/** TODO */
fun testNeverEnding() {
    val lifetime = Lifetime.createNeverEnding()
    var destructorCalled = false
    var isAliveCalled = false

    assert(lifetime.isAlive) { "Lifetime was unexpectedly not alive" }

    val binding1 = lifetime.onLifetimeEnded { destructorCalled = true }
    assert(!destructorCalled) { "A destructor was called in onLifetimeEnded" }

    val binding2 = lifetime.onLifetimeEndedIfAlive(ifAlive = { isAliveCalled = true })
        { destructorCalled = true }
    assert(!destructorCalled) { "A destructor was called in onLifetimeEndedIfAlive" }
    assert(isAliveCalled) { "ifAlive callback was not called" }

    assert(binding1 != null) { "null binding returned from onLifetimeEnded" }
    assert(binding2 != null) { "null binding returned from onLifetimeEndedIfAlive" }

    binding1!!.disconnect()
    binding2!!.disconnect()
}
