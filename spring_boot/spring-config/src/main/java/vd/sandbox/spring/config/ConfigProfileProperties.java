package vd.sandbox.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import vd.sandbox.spring.config.api.IProfileProperties;
import vd.sandbox.spring.config.api.util.YamlPropertySourceFactory;

@Configuration
@ConfigurationProperties
@PropertySources({
    @PropertySource(value = "classpath:profiledProperties.yml", factory = YamlPropertySourceFactory.class),
    @PropertySource(value = "classpath:profiledProperties-${spring.profiles.active}.yml", factory = YamlPropertySourceFactory.class),
})
public class ConfigProfileProperties implements IProfileProperties {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigProfileProperties.class);

  private String staticProperty;
  private String dynamicEnvironment;
  private String dynamicMessage;

  public void setStaticProperty(String staticProperty) {
    this.staticProperty = staticProperty;
  }

  @Override
  public String getDynamicEnvironment() {
    return dynamicEnvironment;
  }

  public void setDynamicEnvironment(String dynamicEnvironment) {
    this.dynamicEnvironment = dynamicEnvironment;
  }

  @Override
  public String getDynamicMessage() {
    return dynamicMessage;
  }

  public void setDynamicMessage(String dynamicMessage) {
    this.dynamicMessage = dynamicMessage;
  }

  @Override
  public String getStaticProperty() {
    return staticProperty;
  }

}
