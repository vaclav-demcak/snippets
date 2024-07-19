package vd.sandbox.spring.openapi.aaa;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  private static final String SCHEME_NAME = "basicAuth";
  private static final String SCHEME = "basic";

  @Autowired
  private BuildProperties buildProperties;

  @Bean
  public OpenAPI customOpenAPI() {
    OpenAPI openApi = new OpenAPI().info(getInfo());
    addSecurity(openApi);
    return openApi;
  }

  private Info getInfo() {
    return new Info()
        .title("API Security Services Documentation")
        .description("Swagger dokumentacia k Servisom so zakladnymi AAA servismi")
        .version(buildProperties.getVersion())
        .license(getLicense())
        .contact(new Contact().name("Vaclav Demcak").email("vaclav.demcak@gmail.com"))
        ;
  }

  private License getLicense() {
    return new License()
        .name("Unlicense")
        .url("https://unlicense.org/");
  }

  private void addSecurity(OpenAPI openApi) {
    Components components = createComponents();
    SecurityRequirement securityItem = new SecurityRequirement().addList(SCHEME_NAME);

    openApi
        .components(components)
        .addSecurityItem(securityItem);
  }

  private Components createComponents() {
    Components components = new Components();
    components.addSecuritySchemes(SCHEME_NAME, createSecurityScheme());

    return components;
  }

  private SecurityScheme createSecurityScheme() {
    return new SecurityScheme()
        .name(SCHEME_NAME)
        .type(SecurityScheme.Type.HTTP)
        .scheme(SCHEME);
  }
}
