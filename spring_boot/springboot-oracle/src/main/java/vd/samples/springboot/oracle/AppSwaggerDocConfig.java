package vd.samples.springboot.oracle;

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
    public OpenAPI nisCentralPoiOpenAPI() {
        return new OpenAPI().info(getInfo());
        //        return new OpenAPI().info(getInfo())
        //                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
        //                .components(new Components().addSecuritySchemes(SCHEME_NAME, new SecurityScheme().name(SCHEME_NAME).type(Type.HTTP).scheme(SCHEME).bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi infoApi() {
        return GroupedOpenApi.builder().group("Server Status API").pathsToMatch("/v*/app-info/**").build();
    }


    public Info getInfo() {
        Map<String, Object> extensionsMap = new HashMap()
        {{
            put("Group", buildProperties.getGroup());
            put("Artifact", buildProperties.getArtifact());
            put("AppName", buildProperties.getName());
        }};
        return new Info().title("SpringBoot Oracle sample")
                .description("Swagger GUI for test SpringBoot Oracle sample Server API services")
                .extensions(extensionsMap)
                .version(buildProperties.getVersion())
                .license(getLicense());
    }


    private License getLicense() {
        return new License()
                .name("ↄ⃝  Copyleft 2023 VD Slovakia. All bugs reserved; ")
                .url("");
    }
}
