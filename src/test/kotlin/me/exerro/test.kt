package me.exerro.observables

import me.exerro.exportedFunction

/** Test that the exported function returns 42. */
fun main() {
    assert(exportedFunction() == 42)
}
