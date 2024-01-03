rootProject.name = "SillyUHC-Development"
include(":PlayerStatsAPI", ":SillyUHC", ":ReaperUtils")

pluginManagement{
    repositories{
        gradlePluginPortal()
        maven{
            name = "EldoNexus"
            url = uri("https://eldonexus.de/repository/maven-public/")
        }
    }
}
