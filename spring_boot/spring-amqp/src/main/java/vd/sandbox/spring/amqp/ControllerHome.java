package vd.sandbox.spring.amqp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home redirection to OpenAPI api documentation
 */
@Controller
public class ControllerHome {

  @RequestMapping("/")
  public String index() {
    return "redirect:swagger-ui.html";
  }

}
