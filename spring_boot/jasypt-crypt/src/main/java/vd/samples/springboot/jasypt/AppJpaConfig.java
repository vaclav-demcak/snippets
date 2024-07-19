package vd.samples.springboot.jasypt;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackages = {"vd.samples.springboot.jasypt.dao.entity"})
@EnableJpaRepositories(basePackages = {"vd.samples.springboot.jasypt.dao"})
@EnableTransactionManagement
public class AppJpaConfig {

}
