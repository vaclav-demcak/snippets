package vd.samples.springboot.jasypt;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppSwaggerDocConfig {

    private static final Logger LOG = LoggerFactory.getLogger(AppSwaggerDocConfig.class);

    private static final String SCHEME_NAME = "bearerAuth";
    private static final String SCHEME = "bearer";

    @Autowired
    private BuildProperties buildProperties;


    @PostConstruct
    public void postInitialization() {
        LOG.info("Swagger's API-Group menu is configured ...");
    }


    @Bean
    public OpenAPI appOpenAPI() {
        return new OpenAPI().info(getInfo());
        //        return new OpenAPI().info(getInfo())
        //                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
        //                .components(new Components().addSecuritySchemes(SCHEME_NAME, new SecurityScheme().name(SCHEME_NAME).type(Type.HTTP).scheme(SCHEME).bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi infoApi() {
        return GroupedOpenApi.builder().group("Server Status API").pathsToMatch("/v*/app-info/**").build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder().group("User Service API").pathsToMatch("/v*/users/**").build();
    }

    @Bean
    public GroupedOpenApi cryptApi() {
        return GroupedOpenApi.builder().group("Crypt Service API").pathsToMatch("/v*/cryptUserCredentials/**").build();
    }

    public Info getInfo() {
        return new Info().title("Jasyp test Pilot Server API")
                .description("Swagger GUI for test Pilot Server API services")
                .extensions(new HashMap<String, Object>() {{
                    put("Group", buildProperties.getGroup());
                    put("Artifact", buildProperties.getArtifact());
                    put("AppName", buildProperties.getName());
                }})
                .version(buildProperties.getVersion())
                .license(getLicense());
    }


    private License getLicense() {
        return new License()
                .name("ↄ⃝  Copyleft 2023 VD Slovakia. All bugs reserved; ")
                .url("");
    }
}

