plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
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
    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT") {
        exclude(group = "net.kyori")
    }
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("dev.dejvokep:boosted-yaml:1.3.6")
    implementation("org.openjdk.nashorn:nashorn-core:15.3")
    compileOnly(project(":core"))
    compileOnly(project(":common"))
    compileOnly("io.github.revxrsal:lamp.common:$lamp")
    compileOnly("io.github.revxrsal:lamp.bukkit:$lamp")
}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
