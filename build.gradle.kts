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
    dependsOn(":PlayerStatsAPI:shadowJar")
    dependsOn(":SillyUHC:shadowJar")
}


subprojects {
    if (project.name.contains("PlayerStatsAPI")) {
        apply {
            plugin<PublishData>()
            plugin<MavenPublishPlugin>()
        }
    }
    apply {
        plugin<ShadowPlugin>()
        plugin<JavaPlugin>()
    }

    repositories {
        mavenCentral()
        maven("https://eldonexus.de/repository/maven-public/")
        maven("https://eldonexus.de/repository/maven-proxies/")
    }

    dependencies {
        compileOnly("io.papermc.paper", "paper-api", "1.19.4-R0.1-SNAPSHOT")
        implementation("net.kyori", "adventure-platform-bukkit", "4.3.0")
        implementation("org.json", "json", "20230618")
    }
}