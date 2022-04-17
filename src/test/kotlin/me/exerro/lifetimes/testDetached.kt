package me.exerro.lifetimes

/** Test that lifetimes created with [createDetached] behave as documented. */
fun testDetached() {
    val lifetime = Lifetime.createDetached()
    var destructorCalled1 = false
    var destructorCalled2 = false
    var destructorCalled3 = false
    var destructorCalled4 = false
    var destructorCalled5 = false
    var ifAliveCalled1 = false
    var ifAliveCalled2 = false

    val binding1 = lifetime.onLifetimeEnded { destructorCalled1 = true }
    val binding2 = lifetime.onLifetimeEnded { destructorCalled2 = true }
    val binding3 = lifetime.onLifetimeEndedIfAlive(ifAlive = { ifAliveCalled1 = true }) { destructorCalled3 = true }
    val binding4 = lifetime.onLifetimeEndedIfAlive { destructorCalled4 = true }

    assert(binding1 != null) { "onLifetimeEnded binding was null" }
    assert(binding2 != null) { "onLifetimeEnded binding was null" }
    assert(binding3 != null) { "onLifetimeEndedIfAlive binding was null" }
    assert(binding4 != null) { "onLifetimeEndedIfAlive binding was null" }
    assert(!destructorCalled1) { "onLifetimeEnded destructor called prematurely" }
    assert(!destructorCalled2) { "onLifetimeEnded destructor called prematurely" }
    assert(!destructorCalled3) { "onLifetimeEnded destructor called prematurely" }
    assert(!destructorCalled4) { "onLifetimeEnded destructor called prematurely" }
    assert(ifAliveCalled1) { "ifAlive callback was not called" }

    binding1!!.disconnect()
    binding4!!.disconnect()
    lifetime.end()

    assert(!destructorCalled1) { "onLifetimeEnded destructor called after being disconnected" }
    assert(destructorCalled2) { "onLifetimeEnded destructor not called after lifetime ended" }
    assert(destructorCalled3) { "onLifetimeEndedIfAlive destructor not called after lifetime ended" }
    assert(!destructorCalled4) { "onLifetimeEndedIfAlive destructor called after being disconnected" }

    lifetime.onLifetimeEndedIfAlive(ifAlive = { ifAliveCalled2 = true }) { destructorCalled5 = true }
    assert(!destructorCalled5) { "onLifetimeEndedIfAlive destructor called when lifetime already ended" }
    assert(!ifAliveCalled2) { "onLifetimeEndedIfAlive ifAlive callback called when lifetime already ended" }

    lifetime.onLifetimeEnded { destructorCalled5 = true }
    assert(destructorCalled5) { "onLifetimeEnded destructor not called when lifetime ended" }
}
