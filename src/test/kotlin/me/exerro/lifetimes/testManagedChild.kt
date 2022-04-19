package me.exerro.lifetimes

/** Test that lifetimes created with [createManagedChildOf] behave as documented. */
fun testManagedChild() {
    val a = Lifetime.createDetached()
    val b = Lifetime.createManagedChildOf(a)
    val c = Lifetime.createManagedChildOf(a)

    var destructorCalled1 = 0
    var destructorCalled2 = 0

    b.onLifetimeEnded { destructorCalled1++ }
    c.onLifetimeEnded { destructorCalled2++ }

    assert(destructorCalled1 == 0)
    assert(destructorCalled2 == 0)

    b.end()

    assert(destructorCalled1 == 1)

    a.end()

    assert(destructorCalled1 == 1)
    assert(destructorCalled2 == 1)
}
