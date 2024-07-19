package vd.sandbox.spring.openapi.aaa.api.repo;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;
import vd.sandbox.spring.openapi.aaa.model.ApplicationInfoDto;

@Component
public class UserRepository {

  private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);

  @Autowired
  private BuildProperties buildProperties;

  @PostConstruct
  public void postInit() {
    LOG.info("UserRepository was initialized successfully...");
  }

  /**
   * Returns msg for User role only
   *
   * @return msg
   */
  public String userWelcomeMsg() {
    LOG.debug("userWelcomeMsg method call ...");
    return "AAA aplikacia ta vita user ";
  }

  /**
   * Returns {@link ApplicationInfoDto} objects with base build properties
   *
   * @return {@link ApplicationInfoDto} object
   */
  public ApplicationInfoDto userInfo() {
    LOG.debug("userInfo method call ...");
    ApplicationInfoDto dto = new ApplicationInfoDto();
    dto.setName(buildProperties.getName());
    dto.setVersion(buildProperties.getVersion());
    dto.setBuildedIn(buildProperties.getTime().toString());
    dto.setArifactGroup(buildProperties.getGroup());
    dto.setArtifactName(buildProperties.getArtifact());
    return dto;
  }
}
