plugins {
    id("io.papermc.paperweight.userdev") version "1.5.4"
}

group = "com.github.redreaperlp.utils"
version = "1.0"


dependencies {
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
}

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