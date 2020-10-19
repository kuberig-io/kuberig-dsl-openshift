[![KubeRig Logo](https://github.com/teyckmans/kuberig-io/blob/master/docs/images/website_logo_transparent_background.png)](https://kuberig.io)

[![kuberig-io](https://circleci.com/gh/kuberig-io/kuberig-dsl-openshift.svg?style=svg)](https://app.circleci.com/pipelines/github/kuberig-io/kuberig-dsl-openshift)

# kuberig-dsl-kubernetes

This project contains the vanilla OpenShift DSL generator projects.

## Details

The modules in this project are auto generated based on the openshift/origin repository.

It scans all tags that contains the api/swagger-spec/openshift-openapi-spec.json file that we depend on for the DSL generation.

You can check the DslProjectsGeneratorTask in the buildSrc for more details.

## Versioning

This project follows the version of the https://github.com/kuberig-io/kuberig-dsl project.