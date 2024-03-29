import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.7.21"
    `maven-publish`
}

allprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "me.exerro"
            artifactId = "lifetimes-kt"
            version = "1.2.1"

            from(components["java"])
        }
    }
}

allprojects {
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.contracts.ExperimentalContracts"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime"
        kotlinOptions.freeCompilerArgs += "-Xcontext-receivers"
        kotlinOptions.freeCompilerArgs += "-language-version"
        kotlinOptions.freeCompilerArgs += "1.7"
    }
}
