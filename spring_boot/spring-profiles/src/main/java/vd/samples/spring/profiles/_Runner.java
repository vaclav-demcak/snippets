package vd.samples.spring.profiles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class _Runner {

    public static void main(String[] args) {
		SpringApplication application = new SpringApplication(_Runner.class);
//		application.setAdditionalProfiles("bar");
		application.run(args);
	}

}
