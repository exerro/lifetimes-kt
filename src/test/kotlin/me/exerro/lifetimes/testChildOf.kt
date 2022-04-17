package me.exerro.lifetimes

/** TODO */
fun testChildOf() {
    val a = Lifetime.createDetached()
    val b = Lifetime.createDetached()
    val abAny = Lifetime.createChildOf(a, b, all = false)
    val abAll = Lifetime.createChildOf(a, b, all = true)

    var anyEnded = 0
    var allEnded = 0

    abAny.onLifetimeEnded { anyEnded++ }
    abAll.onLifetimeEnded { allEnded++ }

    a.end()
    assert(anyEnded == 1) { "'any' lifetime was not ended" }
    assert(allEnded == 0) { "'all' lifetime was ended prematurely" }

    a.end()
    assert(anyEnded == 1) { "'any' lifetime ended twice" }

    b.end()
    assert(anyEnded == 1) { "'any' lifetime twice" }
    assert(allEnded == 1) { "'all' lifetime was not ended" }

    b.end()
    assert(allEnded == 1) { "'all' lifetime was ended twice" }

    try {
        Lifetime.createChildOf(emptyList())
        error("Exception not thrown when using createChildOf with no parent lifetimes")
    }
    catch (e: IllegalArgumentException) {
        assert(e.message?.contains("no lifetimes were given") ?: false) { "Incorrect exception message: ${e.message}" }
    }
    catch (e: Throwable) {
        error("Wrong exception was thrown: ${e.javaClass.name}")
    }
}
