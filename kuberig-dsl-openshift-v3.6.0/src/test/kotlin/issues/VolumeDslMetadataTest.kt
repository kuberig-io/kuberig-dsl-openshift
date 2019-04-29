package issues

import kinds.v1.deploymentConfig
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

/**
 * Check to verify that https://github.com/teyckmans/kuberig-dsl/issues/7 is solved.
 */
class VolumeDslMetadataTest {

    @Test
    fun checkVolumeDslMetadataIsNotRequired() {

        val deploymentConfig = deploymentConfig {
            metadata {
                name("issue#7")
            }
            spec {
                template {
                    metadata {
                        labels {
                            label("a-label", "something-specific")
                        }
                    }
                    spec {
                        containers {
                            container {
                                name("mysql")
                                image("mysql:5.6")
                                ports {
                                    port {
                                        containerPort(5432)
                                        name("mysql")
                                    }
                                }
                                volumeMounts {
                                    volumeMount {
                                        name("db-storage")
                                        mountPath("/var/lib/mysql")
                                    }
                                }
                            }
                        }
                        volumes {
                            volume {
                                name("db-storage")
                                persistentVolumeClaim {
                                    claimName("mysql-db-storage")
                                }

                                // without metadata { } would fail
                            }
                        }
                    }
                }
                strategy {
                    type("Rolling")
                }
                replicas(2)
            }
        }

        // with an NPE here
        assertNotNull(deploymentConfig.toValue())

    }

}