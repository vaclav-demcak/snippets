package vd.sandbox.spring.openapi.citrus;

import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan(basePackages = {"vd.sandbox.spring.openapi.citrus", "vd.sandbox.spring.openapi.citrus.api",
    "vd.sandbox.spring.openapi.citrus.invoker", "vd.sandbox.spring.openapi.citrus.comp"})
//public class _SampleSpringBootRunner {
//
//  public static void main(String[] args) {
//    SpringApplication.run(_SampleSpringBootRunner.class, args);
//  }
public class _SampleSpringBootRunner extends SpringBootServletInitializer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/3.14.2/");
  };

  @Bean
  public Module jsonNullableModule() {
    return new JsonNullableModule();
  }
}

public class _SampleSpringBootRunner implements CommandLineRunner {

  public static void main(String[] args) throws Exception {
    new SpringApplication(_SampleSpringBootRunner.class).run(args);
  }

  @Override
  public void run(String... arg0) throws Exception {
    if (arg0.length > 0 && arg0[0].equals("exitcode")) {
      throw new _SampleSpringBootRunner.ExitException();
    }
  }

  static class ExitException extends RuntimeException implements ExitCodeGenerator {
    private static final long serialVersionUID = 1L;

    @Override
    public int getExitCode() {
      return 10;
    }
  }

  @Bean
  public WebMvcConfigurer webConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("*")
            .allowedHeaders("Content-Type");
      }
    };
  }

}
