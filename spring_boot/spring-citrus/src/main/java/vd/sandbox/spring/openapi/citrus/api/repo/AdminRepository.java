package vd.sandbox.spring.openapi.citrus.api.repo;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;
import vd.sandbox.spring.openapi.citrus.model.ApplicationInfoDto;

@Repository
public class AdminRepository {

  private static final Logger LOG = LoggerFactory.getLogger(AdminRepository.class);

  @Autowired
  private BuildProperties buildProperties;

  @PostConstruct
  public void postInit() {
    LOG.info("AdminRepository was initialized successfully...");
  }

  /**
   * Returns msg for Admin role only
   *
   * @return msg
   */
  public String adminWelcomeMsg() {
    LOG.debug("adminWelcomeMsg method call ...");
    return "AAA aplikacia ta vita administrator ";
  }

  /**
   * Returns {@link ApplicationInfoDto} full objects
   *
   * @return {@link ApplicationInfoDto} object
   */
  public ApplicationInfoDto adminInfo() {
    LOG.debug("adminInfo method call ...");
    ApplicationInfoDto dto = new ApplicationInfoDto();
    dto.setName(buildProperties.getName());
    dto.setVersion(buildProperties.getVersion());
    dto.setBuildedIn(buildProperties.getTime().toString());
    dto.setArifactGroup(buildProperties.getGroup());
    dto.setArtifactName(buildProperties.getArtifact());
    dto.setGitHash(buildProperties.get("git.hash"));
    dto.setUserName(buildProperties.get("builded.by"));
    return dto;
  }
}
