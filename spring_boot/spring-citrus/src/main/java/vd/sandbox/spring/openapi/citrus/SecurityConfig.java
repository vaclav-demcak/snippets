package vd.sandbox.spring.openapi.citrus;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String[] SWAGGER_WHITELIST = {
      "/",
      "/v3/api-docs/**",
      "/swagger-ui/**",
      "/swagger-ui.html",
      "/aaa/v1/guest/services/**",
  };

//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http
//        .cors()
//        .and()
//        .authorizeRequests()
//        .antMatchers(SWAGGER_WHITELIST).permitAll()
//        .anyRequest().authenticated()
//        .and()
//        .httpBasic();
//  }
}
