
plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "io.github.krys"
version = "1.0.0"
val suffix: String? = "alpha.1"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.extendedclip.com/releases/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
    implementation(project(":common"))
    implementation(project(":core"))
    implementation(project(":modules"))

    implementation("dev.dejvokep:boosted-yaml:1.3.6")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "org.bukkit", module = "bukkit")
    }

    compileOnly("io.github.miniplaceholders:miniplaceholders-api:3.0.1")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks {
    processResources {
        filesMatching("plugin.yml") {
            expand(mapOf(
                "version" to project.version,
                "name" to project.name
            ))

            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
    }
    runServer {
        minecraftVersion("1.21")
        downloadPlugins {
            hangar("ViaVersion", "5.3.2")
            hangar("PlaceholderAPI", "2.11.7")
            url("https://ci.lucko.me/job/spark/505/artifact/spark-bukkit/build/libs/spark-1.10.155-bukkit.jar")
           // url("https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar")
            url("https://github.com/MiniPlaceholders/MiniPlaceholders/releases/download/3.1.0/MiniPlaceholders-Paper-3.1.0.jar")
        }
    }
    shadowJar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        relocate("dev.dejvokep.boostedyaml", "io.github.krys.nextrtp.libs")
        if (suffix != null) version = "$version-$suffix"
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}