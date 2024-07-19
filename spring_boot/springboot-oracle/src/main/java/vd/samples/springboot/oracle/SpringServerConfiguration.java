package vd.samples.springboot.oracle;

import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.MessageSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.support.ReloadableResourceBundleMessageSource;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
//@EnableJpaRepositories(basePackages = {"vd.springboot.sample.oracle.repos"})
//@EntityScan("vd.springboot.sample.oracle.model.**")
@EnableCaching
//@EnableAsync
@EnableWebMvc
//@EnableScheduling
public class SpringServerConfiguration implements WebMvcConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(SpringServerConfiguration.class);

    @Value("${cors.allow.all.origins}")
    private Boolean corsAllowAllOrigins;

    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
//    @Autowired
//    private vd.springboot.sample.oracle.server.AppSwaggerDocConfig docConfig;


    @PostConstruct
    public void initialization() {
        LOG.info("... initialization done ...");
    }


    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:i18n/messages", "classpath:i18n/rule-messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(300);
        return messageSource;
    }


//    @Override
//    public LocalValidatorFactoryBean getValidator() {
//        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
//        bean.setValidationMessageSource(messageSource());
//        return bean;
//    }


//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }


    // TODO hack pre Jackson konfiguraciu kvoli WebMvc
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (int i = 0; i < converters.size(); i++) {
            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                converters.set(i, mappingJackson2HttpMessageConverter);
            }
        }
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (Boolean.TRUE.equals(corsAllowAllOrigins)) {
            registry.addMapping("/**").allowedOrigins("*");
        }
    }

}
