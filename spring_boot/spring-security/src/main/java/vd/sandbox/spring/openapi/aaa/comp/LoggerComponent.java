package vd.sandbox.spring.openapi.aaa.comp;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
public class LoggerComponent {

  private static final Logger LOG = LoggerFactory.getLogger(LoggerComponent.class);

  @PostConstruct
  public void postInit() {
    LOG.trace("TRACE log message");
    LOG.debug("DEBUG log message");
    LOG.info("INFO log message");
    LOG.warn("WARN log message");
    LOG.error("ERROR log message");
    additionalLoggingSamples();
  }

  private void additionalLoggingSamples() {
    String param = "SomePram";
    Integer intParam = 5;
    LOG.info("INFO log message with param : {}", param);
    LOG.info("INFO log message with intParam: {} and StringParam {}", intParam, param);
    Exception e = new Exception("SimpleException");
    LOG.error("ERROR log message with exception", e);
    LOG.info("----------- oddelovac ------------");
    LOG.error("ERROR log message with param \"{}\" and exception", param, e);
  }

}
