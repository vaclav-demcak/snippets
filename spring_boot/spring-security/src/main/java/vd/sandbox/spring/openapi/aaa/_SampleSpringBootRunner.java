package vd.sandbox.spring.openapi.aaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"vd.sandbox.spring.openapi.aaa", "vd.sandbox.spring.openapi.aaa.api",
    "vd.sandbox.spring.openapi.aaa.invoker", "vd.sandbox.spring.openapi.aaa.comp"})
public class _SampleSpringBootRunner {

  public static void main(String[] args) {
    SpringApplication.run(_SampleSpringBootRunner.class, args);
  }
}
