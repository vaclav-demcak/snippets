package vd.samples.spring.profiles;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("master")
public class MasterConfiguration {

}
