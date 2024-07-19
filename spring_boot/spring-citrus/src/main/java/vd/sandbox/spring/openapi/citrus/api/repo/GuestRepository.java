package vd.sandbox.spring.openapi.citrus.api.repo;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;
import vd.sandbox.spring.openapi.citrus.model.ApplicationInfoDto;

@Component
public class GuestRepository {

  private static final Logger LOG = LoggerFactory.getLogger(GuestRepository.class);

  @Autowired
  private BuildProperties buildProperties;

  @PostConstruct
  public void postInit() {
    LOG.info("GuestRepository was initialized successfully...");
  }

  /**
   * Returns msg for Guest role
   *
   * @return msg
   */
  public String guestWelcomeMsg() {
    LOG.debug("guestWelcomeMsg method call ...");
    return "AAA aplikacia ta vita Guest ";
  }

  /**
   * Returns {@link ApplicationInfoDto} with app name property only
   *
   * @return {@link ApplicationInfoDto} object
   */
  public ApplicationInfoDto guestInfo() {
    LOG.debug("guestInfo method call ...");
    ApplicationInfoDto dto = new ApplicationInfoDto();
    dto.setName(buildProperties.getName());
    return dto;
  }
}
