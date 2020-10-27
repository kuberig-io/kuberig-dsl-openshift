import io.kuberig.dsl.vanilla.plugin.SemVersion

plugins {
    id("io.kuberig.dsl.vanilla.plugin")
}

kubeRigDslVanilla {
    gitHubOwner = "openshift"
    gitHubRepo = "origin"
    startVersion = SemVersion(3, 9, 0)
    swaggerLocation = "api/swagger-spec/openshift-openapi-spec.json"
}
