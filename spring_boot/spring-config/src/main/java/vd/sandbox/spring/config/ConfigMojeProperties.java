package vd.sandbox.spring.config;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import vd.sandbox.spring.config.api.util.YamlPropertySourceFactory;

@Configuration
@ConfigurationProperties
@PropertySource(value = "classpath:mojeProperties.yml", factory = YamlPropertySourceFactory.class)
public class ConfigMojeProperties {

  private String meno;
  private String priezvisko;

  public String getMeno() {
    return meno;
  }

  public void setMeno(String meno) {
    this.meno = meno;
  }

  public String getPriezvisko() {
    return priezvisko;
  }

  public void setPriezvisko(String priezvisko) {
    this.priezvisko = priezvisko;
  }

  @PostConstruct
  public void postInit() {
    LOG.info("Moje-Properties MENO : {}", meno);
    LOG.info("Moje-Properties PRIEZVISKO : {}", priezvisko);
  }

  private static final Logger LOG = LoggerFactory.getLogger(ConfigMojeProperties.class);
}
