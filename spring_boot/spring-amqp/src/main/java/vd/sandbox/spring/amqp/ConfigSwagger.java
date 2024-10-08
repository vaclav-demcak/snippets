package vd.sandbox.spring.amqp;

import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.Paths;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class ConfigSwagger {

  ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Spring Swagger Simple Sample API Documentation")
        .description("Simple Stupid Message from SpringBoot REST endpoint.")
        .license("")
        .licenseUrl("http://unlicense.org")
        .termsOfServiceUrl("")
        .version("1.0.0")
        .contact(new Contact("Vaclav Demcak","", "vaclav.demcak@gmail.com"))
        .build();
  }

  @Bean
  public Docket customImplementation(ServletContext servletContext, @Value("${base-path:}") String basePath) {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("vd.sandbox.spring.amqp.api"))
        .build()
        .pathProvider(new BasePathAwareRelativePathProvider(servletContext, basePath))
        .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
        .directModelSubstitute(java.time.OffsetDateTime.class, java.util.Date.class)
        .apiInfo(apiInfo());
  }

  class BasePathAwareRelativePathProvider extends RelativePathProvider {
    private String basePath;

    public BasePathAwareRelativePathProvider(ServletContext servletContext, String basePath) {
      super(servletContext);
      this.basePath = basePath;
    }

    @Override
    protected String applicationPath() {
      return  Paths.removeAdjacentForwardSlashes(
          UriComponentsBuilder.fromPath(super.applicationPath()).path(basePath).build().toString());
    }

    @Override
    public String getOperationPath(String operationPath) {
      UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/");
      return Paths.removeAdjacentForwardSlashes(
          uriComponentsBuilder.path(operationPath.replaceFirst("^" + basePath, "")).build().toString());
    }
  }
}
