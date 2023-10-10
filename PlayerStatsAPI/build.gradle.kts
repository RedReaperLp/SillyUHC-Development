import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

group = "com.github.redreaperlp.psa"
version = "1.0.0"

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
            setUrl(publishData.getRepository())
        }
    }
}

dependencies {
    implementation("com.zaxxer", "HikariCP", "5.0.1")
    implementation("org.mariadb.jdbc", "mariadb-java-client", "3.2.0")
    implementation(project(mapOf("path" to ":ReaperUtils")))
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")
        archiveFileName.set("PS-API.jar")
    }
}

tasks.getByName("shadowJar").finalizedBy("copyFile")

tasks.register<Copy>("copyFile") {
    from("build/libs/PS-API.jar")
    into("E:\\Minecraft_Server\\1.19\\1.19.4\\Paper\\5\\plugins")
}

tasks.register<Wrapper>("wrapper") {
    gradleVersion = "5.6.4"
}

tasks.register("prepareKotlinBuildScriptModel"){}