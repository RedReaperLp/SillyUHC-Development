rootProject.name = "SillyUHC-Development"
include(":PlayerStatsAPI", ":SillyUHC")

pluginManagement{
    repositories{
        gradlePluginPortal()
        maven{
            name = "EldoNexus"
            url = uri("https://eldonexus.de/repository/maven-public/")
        }
    }
}