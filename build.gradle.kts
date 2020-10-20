import io.kuberig.dsl.vanilla.plugin.KubeRigDslVanillaPluginExtension
import io.kuberig.dsl.vanilla.plugin.SemVersion

buildscript {
    repositories {
        jcenter()
        maven("https://dl.bintray.com/teyckmans/rigeldev-oss-maven")
    }
    dependencies {
        classpath("io.kuberig:kuberig-dsl-generator-gradle-plugin:${version}")
        classpath("io.kuberig.dsl.vanilla.plugin:kuberig-dsl-vanilla-plugin:0.2.4")
    }
}

apply(plugin = "io.kuberig.dsl.vanilla.plugin")

configure<KubeRigDslVanillaPluginExtension> {
    gitHubOwner = "openshift"
    gitHubRepo = "origin"
    startVersion = SemVersion(3, 9, 0)
    swaggerLocation = "api/swagger-spec/openshift-openapi-spec.json"
}
