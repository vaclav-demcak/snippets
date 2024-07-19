package vd.sandbox.spring.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"vd.sandbox.spring.config", "vd.sandbox.spring.config.api"})
public class SampleSpringBootRunner {

  public static void main(String[] args) throws Exception {
    new SpringApplication(SampleSpringBootRunner.class).run(args);
  }
}
