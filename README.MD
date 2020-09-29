[![KubeRig Logo](https://github.com/teyckmans/kuberig/blob/master/docs/images/website_logo_transparent_background.png)](https://github.com/teyckmans/kuberig)

[![buddy pipeline](https://app.buddy.works/rigeldev/kuberig-dsl-openshift/pipelines/pipeline/281755/badge.svg?token=510082a4eefd24375f16aca82e81379d132451ac153bd488e8d7f5ed0f31311e "buddy pipeline")](https://app.buddy.works/rigeldev/kuberig-dsl-openshift/pipelines/pipeline/281755)

# kuberig-dsl-kubernetes

This project contains the vanilla OpenShift DSL generator projects.

## Details

The modules in this project are auto generated based on the openshift/origin repository.

It scans all tags that contains the api/swagger-spec/openshift-openapi-spec.json file that we depend on for the DSL generation.

You can check the DslProjectsGeneratorTask in the buildSrc for more details.

## Versioning

This project follows the version of the https://github.com/teyckmans/kuberig-dsl project.