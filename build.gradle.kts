import com.jfrog.bintray.gradle.BintrayExtension
import io.kuberig.dsl.vanilla.plugin.KubeRigDslVanillaPluginExtension
import io.kuberig.dsl.vanilla.plugin.SemVersion

buildscript {
    repositories {
        jcenter()
        maven("https://dl.bintray.com/teyckmans/rigeldev-oss-maven")
    }
    dependencies {
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
        classpath("eu.rigeldev.kuberig:kuberig-dsl-generator-gradle-plugin:0.0.21")
        classpath("io.kuberig.dsl.vanilla.plugin:kuberig-dsl-vanilla-plugin:0.1.1")
    }
}

apply(plugin = "io.kuberig.dsl.vanilla.plugin")

configure<KubeRigDslVanillaPluginExtension> {
    gitHubOwner = "openshift"
    gitHubRepo = "origin"
    startVersion = SemVersion(3, 9, 0)
    swaggerLocation = "api/swagger-spec/openshift-openapi-spec.json"
}

subprojects {
    apply {
        plugin("com.jfrog.bintray")
        plugin("maven-publish")
        plugin("java")
        plugin("idea")
    }

    val subProject = this

    subProject.group = "eu.rigeldev.kuberig.dsl.openshift"
    subProject.version = project.version

    repositories {
        jcenter()
    }

    dependencies {
        val testImplementation by configurations
        val testRuntimeOnly by configurations

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.2")
    }

    val sourcesJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")

        val sourceSets: SourceSetContainer by subProject
        from(sourceSets["main"].allSource)
    }

    configure<PublishingExtension> {

        publications {
            register(subProject.name, MavenPublication::class) {
                from(components["java"])
                artifact(sourcesJar.get())
            }
        }

    }

    configure<BintrayExtension> {
        val bintrayApiKey : String by subProject
        val bintrayUser : String by subProject

        user = bintrayUser
        key = bintrayApiKey
        publish = true

        pkg(closureOf<BintrayExtension.PackageConfig>{
            repo = "rigeldev-oss-maven"
            name = subProject.name
            setLicenses("Apache-2.0")
            vcsUrl = "https://github.com/teyckmans/kuberig-dsl-openshift"
        })

        setPublications(subProject.name)
    }
}