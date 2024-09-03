package vd.samples.springboot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "")
@Getter
@Setter
public class FelixConfiguration {

    private Map<String, String> config;
}
