package me.exerro.lifetimes

import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

/** TODO */
fun testWithLifetime() {
    var destructorCalled1 = false
    var destructorCalled2 = false

    assert(withLifetime {
        onLifetimeEnded { destructorCalled1 = true }
        3
    } == 3) { "Return value did not match" }

    suspend {
        withLifetimeSuspend {
            suspend {
                onLifetimeEnded { destructorCalled2 = true }
            } ()
            5
        }
    } .startCoroutine(Continuation(EmptyCoroutineContext) {
        assert(it.isSuccess && it.getOrThrow() == 5) { "Return value did not match" }
    })

    assert(destructorCalled1) { "Scoped lifetime destructor not called" }
    assert(destructorCalled2) { "Suspend scoped lifetime destructor not called" }
}
