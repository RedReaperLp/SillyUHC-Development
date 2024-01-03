import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

group = "com.github.redreaperlp"
version = "1.0.1"

dependencies {
    implementation("com.zaxxer", "HikariCP", "5.0.1")
    implementation("org.mariadb.jdbc", "mariadb-java-client", "3.2.0")
    implementation(project(mapOf("path" to ":ReaperUtils")))
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveFileName.set("PS-API.jar")
    }
}

tasks.getByName("shadowJar").finalizedBy("copyFile").finalizedBy("copyFile2").finalizedBy("copyFile3")

tasks.register<Copy>("copyFile") {
    from("build/libs/PS-API.jar")
    into("E:\\Minecraft_Server\\1.19\\1.19.4\\Paper\\1\\plugins")
}
tasks.register<Copy>("copyFile2") {
    from("build/libs/PS-API.jar")
    into("E:\\Minecraft_Server\\1.19\\1.19.4\\Paper\\2\\plugins")
}
tasks.register<Copy>("copyFile3") {
    from("build/libs/PS-API.jar")
    into("E:\\Minecraft_Server\\1.19\\1.19.4\\Paper\\3\\plugins")
}

tasks.register<Wrapper>("wrapper") {
    gradleVersion = "5.6.4"
}

tasks.register("prepareKotlinBuildScriptModel"){}

publishData {
    useEldoNexusRepos()
    publishComponent("java")
}

publishing {
    publications.create<MavenPublication>("maven") {
        publishData.configurePublication(this)
    }

    repositories {
        maven {
            authentication {
                credentials(PasswordCredentials::class) {
                    username = System.getenv("NEXUS_USERNAME")
                    password = System.getenv("NEXUS_PASSWORD")
                }
            }

            name = "EldoNexus"
            url = uri(publishData.getRepository())
        }
    }
}