rootProject.name = "kuberig-dsl-openshift"

pluginManagement {
    val version : String by settings
    val kuberigDslVanillaPluginVersion : String by settings

    plugins {
        id("io.kuberig.dsl.generator") version(version)
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "io.kuberig.dsl.vanilla.plugin") {
                useModule("io.kuberig.dsl.vanilla.plugin:kuberig-dsl-vanilla-plugin:${kuberigDslVanillaPluginVersion}")
            }
        }
    }

    repositories {
        jcenter()
        maven("https://dl.bintray.com/teyckmans/rigeldev-oss-maven")
        gradlePluginPortal()
    }
}

include("kuberig-dsl-openshift-v3.9.0")
include("kuberig-dsl-openshift-v3.10.0")
include("kuberig-dsl-openshift-v3.11.0")
include("kuberig-dsl-openshift-v4.1.0")
include("kuberig-dsl-openshift-v4.2.0")
include("kuberig-dsl-openshift-v4.3.0")
include("kuberig-dsl-openshift-v4.4.0")
//include("kuberig-dsl-openshift-v4.5.0") openapi spec was NOT updated in the upstream repo.
include("kuberig-dsl-openshift-v4.6.0")