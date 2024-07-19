package vd.samples.spring.profiles;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("foo")
public class FooConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(FooConfiguration.class);

    @PostConstruct
    public void postConstruct(){
        LOG.info("loaded FooConfiguration!");
    }

}
