package vd.sandbox.spring.amqp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"vd.sandbox.spring.amqp", "vd.sandbox.spring.amqp.api"})
public class _SampleSpringBootRunner {

  public static void main(String[] args) throws Exception {
    new SpringApplication(_SampleSpringBootRunner.class).run(args);
  }
}
