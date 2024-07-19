# OpenAPI Generator Playground 

## Overview
This is a boiler-plate project to generate your own project derived from an OpenAPI specification.
Its goal is to get you started with the basic plumbing so you can put in your own logic.
It won't work without your changes applied.

## What's OpenAPI
The goal of OpenAPI is to define a standard, language-agnostic interface to REST APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection.
When properly described with OpenAPI, a consumer can understand and interact with the remote service with a minimal amount of implementation logic.
Similar to what interfaces have done for lower-level programming, OpenAPI removes the guesswork in calling the service.

Check out [OpenAPI-Spec](https://github.com/OAI/OpenAPI-Specification) for additional information about the OpenAPI project, including additional libraries with support for other languages and more. 

## Gradle Sample
This sample shows how to generate a SpringBoot server to propagate the Swagger Endpoint with service's description to [localhost:8080](http://localhost:8080/swagger-ui.html) whit all objects and "dummy -> error generated" controller.

## OpenApi Generator Plugin
> - [Repository URL](https://mvnrepository.com/artifact/org.openapitools/openapi-generator-gradle-plugin)
> - [Generator prop](https://github.com/OpenAPITools/openapi-generator/tree/master/docs/generators)
> - [Generator docs](https://openapi-generator.tech/docs/plugins)
> - [Swagger specif](https://swagger.io/docs/specification/about/)
> - Example of use: 
>   - [OpenAPI samples](https://github.com/OpenAPITools/openapi-generator/tree/master/samples)
>   - [Pet Clinic sample](https://github.com/gantsign/spring-petclinic-openapi)

We are not using Plugin directly because the default task don't catch a target for these tutorial, but everyone is free to edit it as he need

Default OpenAPI Generator Plugin tasks

|Task	            |Description                                |
| ----------------- | ----------------------------------------- |
|openApiGenerate	|Generate code via Open API Tools Generator for Open API 2.0 or 3.x specification documents. |
|openApiGenerators	|Lists generators available via Open API Generators. |                                  
|openApiMeta	    |Generates a new generator to be consumed via Open API Generator. |
|openApiValidate	|Validates an Open API 2.0 or 3.x specification document. |

    The access could be provided by add next line to build.gradle
```
     apply plugin: 'org.openapi.generator'
```

## How do I use this?
The sample has prepared with a default api.yml definition. So everyone could clone the project and starts the default SpringBoot server

```
    ./gradlew bootRun
```
The build starts the SpringBoot server, and it exposes the Swagger EndPoint to localhost:8080 to see what was written in api.yml file.

The Gradle build file contains the definition for own Generator tasks

|Task           |How to Run         |Description                |
| ------------- | ----------------- | ------------------------- |
| buildSpring   | ./gradlew buildSpring | Task generates java springBoot 'dummy' project from api.yml to ./build/spring |
| buildKotlinSpring | ./gradlew buildKotlinSpring | Task generates kotlin springBoot 'dummy' project from api.yml to ./build/spring |
| validateSpec  | ./gradlew validateSpec | Task validates api.yml file |
| makeMeta      | ./gradlew makeMeta | Task generates meta to ./build/meta |
| makeAsciidoc  | ./gradlew makeAsciidoc | Task create asciidoc file 'index.adoc' in ./build/doc |
| makeCwiki     | ./gradlew makeCwiki | Task create the confluence wiki file 'confluence-markup.txt' in ./build/doc |
| makeMarkdown  | ./gradlew makeMarkdown | Task create md files in ./build/docs |

Only 'buildSpring' task joins to java build process by 'compileJava.dependsOn(buildSpring)'.

## So training now
Everyone wants to have own services, so let's make a changes in
 - API_description/swagger/api.yml
 - API_description/components/users.yml

  > ### So wish a good fun  
  
## Training base file distribution
At this point, you've likely generated a client or server setup.
It will include something along these lines:

```
.
|- README.md                                // this file
|- gradle.build                             // build script
|-- API_description
|--- components
|---- users.yml                             // components definitions
|--- swagger
|---- api.yml                               // swagger api definitions
|-- src
|--- main
|---- java
|----- vd.javatorium.openapi.spring.api     // package to add own Controller impl.
|---- resources
|----- templates                            // mustache template files
|----- META-INF
|------ services
|------- org.openapitools.codegen.CodegenConfig
|----- openapi-generator
|------ .openapi-generator-ignore           // add here code for skip in generate process
```

## TODOs
 - wish to add avro-schema task to show how to generate
 - wish to show vertx framework and compare performance
