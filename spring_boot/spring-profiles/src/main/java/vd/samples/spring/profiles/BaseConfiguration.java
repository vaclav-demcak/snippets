package vd.samples.spring.profiles;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import vd.samples.spring.profiles.beans.BarBean;

@Configuration
public class BaseConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(BaseConfiguration.class);

    @Value("${spring.application.name:\"Default Application Name\"}")
    private String applicationName;

    @Autowired
    private Environment env;
    @Autowired
    private OutputPort port;

    @PostConstruct
    public void postConstruct() {
        LOG.info("loaded BaseConfiguration!");
        LOG.info("Active profiles are: " + Arrays.asList(env.getActiveProfiles()));
        LOG.info("Output Port is : " + port.getClass().getSimpleName());
        LOG.info("--------------------------------------------------------------");
        LOG.info("Application " + applicationName + " has started successful !!!");
    }

    @Bean
    @Profile("bar")
    BarBean barBean() {
        return new BarBean();
    }

    @Bean
    @Profile("local")
    OutputPort mockedOutputPort(){
        return new OutputPortMocked();
    }

    @Bean
    @Profile("!local") /* pre vsetky profily okrem local */
    OutputPort realOutputPort(){
        return new OutputPortRead();
    }

}
