package vd.sandbox.spring.swagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"vd.sandbox.spring.swagger", "vd.sandbox.spring.swagger.api"})
public class SampleSpringBootRunner {

  public static void main(String[] args) throws Exception {
    new SpringApplication(SampleSpringBootRunner.class).run(args);
  }
}
