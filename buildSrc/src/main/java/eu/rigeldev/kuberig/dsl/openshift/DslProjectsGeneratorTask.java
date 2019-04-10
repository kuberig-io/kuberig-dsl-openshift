package eu.rigeldev.kuberig.dsl.openshift;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class DslProjectsGeneratorTask extends DefaultTask {

    private String gitHubOwner = "openshift";
    private String gitHubRepo = "origin";

    @TaskAction
    public void generatorDslProjects() throws Exception {
        Unirest.setObjectMapper(new com.mashape.unirest.http.ObjectMapper() {

            private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new ObjectMapper();

            @Override
            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return this.objectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String writeValue(Object value) {
                try {
                    return this.objectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        final HttpResponse<GitHubTagRef[]> tags = Unirest.get("https://api.github.com/repos/{gitHubOwner}/{gitHubRepo}/git/refs/tags")
                .routeParam("gitHubOwner", this.gitHubOwner)
                .routeParam("gitHubRepo", this.gitHubRepo)
                .header("Accept", "application/vnd.github.v3+json")
                .asObject(GitHubTagRef[].class);


        for (GitHubTagRef tag : tags.getBody()) {


            final String tagName = tag.getRef().substring("refs/tags/".length());



            System.out.println("Generating project for tag " + tagName);

            String moduleName = "kuberig-dsl-openshift-" + tagName;
            File moduleDir = new File(moduleName);

            if (!moduleDir.exists()) {

                final GetRequest getRequest = Unirest.get("https://raw.githubusercontent.com/{gitHubOwner}/{gitHubRepo}/{tagName}/api/swagger-spec/openshift-openapi-spec.json")
                        .routeParam("gitHubOwner", this.gitHubOwner)
                        .routeParam("gitHubRepo", this.gitHubRepo)
                        .routeParam("tagName", tagName);
                final String swaggerJsonUrl = getRequest.getUrl();
                final HttpResponse<String> swaggerJson = getRequest
                        .asString();

                if (swaggerJson.getStatus() == 200) {
                    final String swaggerJsonText = swaggerJson.getBody();
                    if (swaggerJsonText.contains("x-kubernetes-group-version-kind")) {
                        System.out.println(tagName + " => VALID ");

                        moduleDir.mkdir();

                        File mainResources = new File(moduleDir, "src/main/resources");
                        mainResources.mkdirs();

                        File swaggerJsonFile = new File(mainResources, "swagger.json");
                        Files.write(swaggerJsonFile.toPath(), swaggerJsonText.getBytes(StandardCharsets.UTF_8));

                        File buildGradleKtsFile = new File(moduleDir, "build.gradle.kts");
                        List<String> buildGradleKtsLines = Arrays.asList("plugins {",
                                "    id(\"eu.rigeldev.kuberig.dsl.generator\") version \""+getProject().getVersion().toString()+"\"",
                                "}",
                                "",
                                "repositories {",
                                "    jcenter()",
                                "}");
                        Files.write(buildGradleKtsFile.toPath(), buildGradleKtsLines);


                        File readmeFile = new File(moduleDir, "README.MD");
                        List<String> readmeLines = Arrays.asList(
                                "# " + moduleName,
                                "",
                                "Swagger file downloaded from " + swaggerJsonUrl
                        );
                        Files.write(readmeFile.toPath(), readmeLines, StandardCharsets.UTF_8);

                        Path mainBuildGradleKts = Paths.get("build.gradle.kts");

                        final List<String> mainBuildGradleKtsLines = Files.readAllLines(mainBuildGradleKts, StandardCharsets.UTF_8);

                        final String lineToAdd = "include(\"" + moduleName + "\")";

                        if (!mainBuildGradleKtsLines.contains(lineToAdd)) {
                            Files.write(mainBuildGradleKts, lineToAdd.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                        }

                    } else {
                        System.out.println(tagName + " => IN-VALID ( does not have x-kubernetes-group-version-kind info )");
                    }
                } else {
                    System.out.println(tagName + " => " + swaggerJson.getStatusText());
                }
            } else {
                File buildGradleKtsFile = new File(moduleDir, "build.gradle.kts");

                List<String> buildGradleKtsLines = Arrays.asList("plugins {",
                        "    id(\"eu.rigeldev.kuberig.dsl.generator\") version \""+getProject().getVersion().toString()+"\"",
                        "}",
                        "",
                        "repositories {",
                        "    jcenter()",
                        "}");

                Files.write(buildGradleKtsFile.toPath(), buildGradleKtsLines);
            }
        }

    }

}
