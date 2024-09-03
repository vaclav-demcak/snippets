package vd.samples.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import vd.samples.springboot.config.FelixConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(FelixConfiguration.class)
public class _ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(_ApplicationRunner.class, args);
    }

}
