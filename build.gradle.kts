import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import de.chojo.PublishData

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("de.chojo.publishdata") version "1.2.4"
    `maven-publish`
}

publishData {
    useEldoNexusRepos()
    publishComponent("java")
}

group = "com.github.redreaperlp"
version = "1.0"

tasks.register("buildAll") {
    finalizedBy(":ReaperUtils:shadowJar")
    finalizedBy(":PlayerStatsAPI:shadowJar")
    finalizedBy(":SillyUHC:shadowJar")
}


subprojects {
    if (project.name.contains("PlayerStatsAPI") || project.name.contains("ReaperUtils")) {
        apply {
            plugin<PublishData>()
            plugin<MavenPublishPlugin>()
        }
    }
    apply {
        plugin<ShadowPlugin>()
        plugin<JavaPlugin>()
    }
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://oss.sonatype.org/content/repositories/central")
        maven("https://eldonexus.de/repository/maven-public/")
        maven("https://eldonexus.de/repository/maven-proxies/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }

    dependencies {
        compileOnly("io.papermc.paper", "paper-api", "1.17.1-R0.1-SNAPSHOT")
        compileOnly("me.clip", "placeholderapi", "2.11.4")
        implementation("net.kyori", "adventure-platform-bukkit", "4.3.0")
        implementation("org.json", "json", "20230618")
    }

    tasks {
        named<JavaCompile>("compileJava") {
            options.encoding = "UTF-8"
        }
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}