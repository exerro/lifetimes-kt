package me.exerro.lifetimes

/** Run all tests in this directory. */
fun main() {
    testNeverEnding()
    testAlreadyEnded()
    testDetached()
    testManagedChild()
    testChildOf()
    testWithLifetime()

    LifetimeBinding.Companion
}
