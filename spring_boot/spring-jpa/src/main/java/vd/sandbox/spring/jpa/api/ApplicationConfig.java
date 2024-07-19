package vd.sandbox.spring.jpa.api;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackages = {"vd.sandbox.spring.jpa.db.model"})
@EnableJpaRepositories(basePackages = {"vd.sandbox.spring.jpa.db.repo"})
@EnableTransactionManagement
public class ApplicationConfig {

}
