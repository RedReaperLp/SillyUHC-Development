import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.5.4"
}

group = "com.github.redreaperlp.sillyuhc"
version = "1.0.1"

dependencies {
    compileOnly(project(mapOf("path" to ":PlayerStatsAPI")))
    implementation(project(mapOf("path" to ":ReaperUtils")))
    paperweight.paperDevBundle("1.17.1-R0.1-SNAPSHOT")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveFileName.set("SillyUHC.jar")
    }
}

tasks.getByName("shadowJar").finalizedBy("copyFile")
        //.finalizedBy("copyFile2").finalizedBy("copyFile3")

tasks.register<Copy>("copyFile") {
    from("build/libs/SillyUHC.jar")
    into("E:\\Minecraft_Server\\1.19\\1.19.4\\Paper\\1\\plugins")
}
//tasks.register<Copy>("copyFile2") {
//    from("build/libs/SillyUHC.jar")
//    into("E:\\Minecraft_Server\\1.19\\1.19.4\\Paper\\2\\plugins")
//}
//tasks.register<Copy>("copyFile3") {
//    from("build/libs/SillyUHC.jar")
//    into("E:\\Minecraft_Server\\1.19\\1.19.4\\Paper\\3\\plugins")
//}

tasks.register<Wrapper>("wrapper") {
    gradleVersion = "5.6.4"
}

tasks.register("prepareKotlinBuildScriptModel"){}