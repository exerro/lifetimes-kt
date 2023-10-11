<h1 align="center">
  lifetimes-kt
</h1>

<p align="center">
  <a href="https://jitpack.io/#exerro/lifetimes-kt"><img src="https://jitpack.io/v/exerro/lifetimes-kt.svg" alt="JitPack badge"/></a>
</p>

A lifetime API to help with resource lifetimes and cleanup.

A lifetime represents a time span for which it is "alive". Destructors can be
attached to a lifetime to be called when the lifetime ends.

```kotlin
withLifetime {
    onLifetimeEnded { println("I was called!") }
    println("I run first.")
    //> I run first.
}
//> I was called!
```

Using Kotlin contexts, this approach can be streamlined and composed:

```kotlin
context (Lifetime)
fun doLivingThing() {
    onLifetimeEnded { println("I was called!") }
}

withLifetime {
    doLivingThing()
    println("I run first.")
    //> I run first.
}
//> I was called!
```

## Installation

Check out the [releases](https://github.com/exerro/lifetimes-kt/releases), or
using a build system...

### Gradle (`build.gradle.kts`)

```kotlin
repositories {
    // ...
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("me.exerro:lifetimes-kt:1.2.1")
}
```

### Maven

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
  <groupId>me.exerro</groupId>
  <artifactId>lifetimes-kt</artifactId>
  <version>1.2.1</version>
</dependency>
```

## Getting started

> The documentation is heavily linked with "see also"s, so take a look at the
> docs for `Lifetime`.

A useful wrapper is `withLifetime`, which introduces a lifetime spanning a block
as shown above. When more control is required, use `Lifetime.createDetached` to
explicitly manage when the lifetime ends.

Sometimes, constant lifetimes might be wanted, for which there exists
`Lifetime.createNeverEnding` and `Lifetime.createAlreadyEnded`.

When there are already lifetimes available, using `Lifetime.createChildOf` can
build a lifetime which relates to some parents.
