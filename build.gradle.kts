import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"
//    id("io.github.revxrsal.zapper") version "1.0.3"
}

group = "kr.kryz.rtp"
version = "1.0.0-beta.1"

//val lampVersion : String = "4.0.0-rc.11"
//val exposed : String = "0.57.0"
//val cloud : String = "2.0.0"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.extendedclip.com/releases/")
    }
    maven {url = uri("https://jitpack.io") }
}

dependencies {
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "org.bukkit", module = "bukkit")
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks.withType<KotlinJvmCompile> {
    compilerOptions {
        javaParameters = true
    }
}
//
//kotlin {
//    jvmToolchain(21)
//}

//zapper {
//    libsFolder = "libs"
//    repositories { includeProjectRepositories() }
//}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}


tasks {
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to project.version)
            expand("name" to project.name)

            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
    }
}