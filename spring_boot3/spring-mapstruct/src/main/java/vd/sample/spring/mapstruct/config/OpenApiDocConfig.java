package vd.sample.spring.mapstruct.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OpenApiDocConfig {

    private static final Logger LOG = LoggerFactory.getLogger(OpenApiDocConfig.class);

    private static final String SCHEME_NAME = "bearerAuth";
    private static final String SCHEME = "bearer";

    @Autowired
    private BuildProperties buildProperties;

    @PostConstruct
    public void postInitialization() {
        LOG.info("Swagger dockets are configured ...");
    }


    @Bean
    public GroupedOpenApi infoApi() {
        return GroupedOpenApi.builder().group("APP Info").pathsToMatch("/v*/app-info/**").build();
    }

    @Bean
    public GroupedOpenApi travelingApi() {
        return GroupedOpenApi.builder().group("APP Traveling").pathsToMatch("/v*/traveling/**").build();
    }

//    @Bean
//    public OpenAPI acgOpenApi() {
//        return new OpenAPI().info(getInfo());
//            .addSecurityItem(new SecurityRequirement().addList(SCHEME_NA))
//    }

    public Info getInfo() {
        Map<String, Object> extensionsMap = new HashMap()
        {{
            put("Group", buildProperties.getGroup());
            put("Artifact", buildProperties.getArtifact());
            put("AppName", buildProperties.getName());
        }};
        return new Info().title("EDI Converter SpringBoot Server API")
                .description("Swagger GUI for test EDI Converter SpringBoot Server API services")
                .extensions(extensionsMap)
                .version(buildProperties.getVersion())
                .license(getLicense());
    }

    private License getLicense() {
        return new License()
                .name(" © Copyright 2025 NSC, Slovakia. All rights reserved; ")
                .url("");
    }

}
