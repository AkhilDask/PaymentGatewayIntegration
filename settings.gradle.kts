pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven{
            setUrl("https://maven.cashfree.com/release")
            content {
                includeGroup("com.cashfree.pg")
            }
        }
    }
}

rootProject.name = "PaymentGateway"
include(":app")
 