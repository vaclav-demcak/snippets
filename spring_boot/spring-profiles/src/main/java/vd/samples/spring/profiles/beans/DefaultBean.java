package vd.samples.spring.profiles.beans;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("default")
public class DefaultBean {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultBean.class);

    @PostConstruct
    public void postConstruct(){
        LOG.info("loaded DefaultBean!");
    }

}
