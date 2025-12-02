plugins {
    id("java")
}

group = "io.github.krys"
val lamp = "4.0.0-rc.12"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("org.ow2.asm:asm:9.1")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("dev.dejvokep:boosted-yaml:1.3.6")
    compileOnly("io.github.revxrsal:lamp.common:$lamp")
    compileOnly("io.github.revxrsal:lamp.bukkit:$lamp")
    compileOnly("io.github.revxrsal:lamp.brigadier:$lamp")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}