package vd.samples.spring.profiles.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HelloBean {

    private static final Logger LOG = LoggerFactory.getLogger(HelloBean.class);

    public HelloBean(@Value("${helloMessage}") String helloMessage) {
        LOG.info(helloMessage);
    }
}
