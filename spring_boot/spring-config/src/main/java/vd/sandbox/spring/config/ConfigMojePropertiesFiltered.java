package vd.sandbox.spring.config;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import vd.sandbox.spring.config.api.util.YamlPropertySourceFactory;

@Configuration
@ConfigurationProperties("moje-property")
@PropertySource(value = "classpath:mojeProperties.yml", factory = YamlPropertySourceFactory.class)
public class ConfigMojePropertiesFiltered {

  private String name;
  private String haluz;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getHaluz() {
    return haluz;
  }

  public void setHaluz(String haluz) {
    this.haluz = haluz;
  }

  @PostConstruct
  public void postInit() {
    LOG.info("Moje-Properties NAME : {}", name);
    LOG.info("Moje-Properties HALUZ : {}", haluz);
  }

  private static final Logger LOG = LoggerFactory.getLogger(ConfigMojePropertiesFiltered.class);
}
