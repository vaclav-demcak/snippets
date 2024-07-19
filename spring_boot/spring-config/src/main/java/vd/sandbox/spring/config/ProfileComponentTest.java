package vd.sandbox.spring.config;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vd.sandbox.spring.config.api.IProfileProperties;

@Component
public class ProfileComponentTest {

  private static final Logger LOG = LoggerFactory.getLogger(ProfileComponentTest.class);

  @Autowired
  private IProfileProperties prop;

  @PostConstruct
  public void initialization() {
    LOG.info("Initialization ...");
    LOG.info("Static Property is : {}", prop.getStaticProperty());
    LOG.info("Dynamic Environment is : {}", prop.getDynamicEnvironment());
    LOG.info("Dynamic Message is : {}", prop.getDynamicMessage());
  }
}
