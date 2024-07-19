package vd.sandbox.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebServiceController {

  private static final Logger LOG = LoggerFactory.getLogger(WebServiceController.class);

  @RequestMapping("/")
	public String index() {
		return "Greetings from VD Spring Boot!";
	}
}
