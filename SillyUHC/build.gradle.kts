import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.5.4"
}

group = "com.github.redreaperlp.sillyuhc"
version = "1.0.0"

dependencies {
    compileOnly(project(mapOf("path" to ":PlayerStatsAPI")))
    implementation(project(mapOf("path" to ":ReaperUtils")))
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveFileName.set("SillyUHC.jar")
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