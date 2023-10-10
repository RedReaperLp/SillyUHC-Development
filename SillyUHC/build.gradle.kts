import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.5.4"
}

group = "com.github.redreaperlp.sillyuhc"
version = "1.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    compileOnly(project(mapOf("path" to ":PlayerStatsAPI")))
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")
        archiveFileName.set("SillyUHC.jar")
    }

    named<JavaCompile>("compileJava") {
        options.encoding = "UTF-8"
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.getByName("shadowJar").finalizedBy("copyFile")

tasks.register<Copy>("copyFile") {
    from("build/libs/SillyUHC.jar")
    into("E:\\Minecraft_Server\\1.19\\1.19.4\\Paper\\5\\plugins")
}

tasks.register<Wrapper>("wrapper") {
    gradleVersion = "5.6.4"
}

tasks.register("prepareKotlinBuildScriptModel"){}