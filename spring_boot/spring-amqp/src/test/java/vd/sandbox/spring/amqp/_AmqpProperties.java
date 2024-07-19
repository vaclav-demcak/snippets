package vd.sandbox.spring.amqp;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vd.sandbox.spring.amqp.api.IAmqpProperties;

public class _AmqpProperties implements IAmqpProperties {

  private static final Logger LOG = LoggerFactory.getLogger(_AmqpProperties.class);

  private static final String PROP_FILE_NAME = "activemq.properties";
  private static final String BROKER_URI_PROP = "amqp.broker.uri";
  private static final String BROKER_USER_PROP = "amqp.broker.user";
  private static final String BROKER_PWD_PROP = "amqp.broker.pwd";
  private static final String BROKER_CLIENT_QUEUE_PROP = "amqp.broker.client-queue";
  private static final String BROKER_SERVER_QUEUE_PROP = "amqp.broker.server-queue";
  private static final String BROKER_TOPIC_PROP = "amqp.broker.topic";
  private static final String BROKER_NOTIF_TOPIC_PROP = "amqp.broker.notif-topic";
  private static final String BROKER_NOTIF_QUEUE_PROP = "amqp.broker.notif-queue";

  private final Properties properties;

  public _AmqpProperties() throws IOException {
    LOG.info("Load Default properties ... ");

    InputStream input = _AmqpProperties.class.getClassLoader().getResourceAsStream(PROP_FILE_NAME);
    properties = new Properties();
    properties.load(input);
  }

  public URI getJmsUri() { return URI.create(properties.getProperty(BROKER_URI_PROP)); }

  public String getJmsUser() {
    return properties.getProperty(BROKER_USER_PROP);
  }

  public String getJmsPwd() {
    return properties.getProperty(BROKER_PWD_PROP);
  }

  public String getJmsServerQueue() {
    return properties.getProperty(BROKER_SERVER_QUEUE_PROP);
  }

  public String getJmsClientQueue() { return properties.getProperty(BROKER_CLIENT_QUEUE_PROP); }

  public String getJmsTopic() {
    return properties.getProperty(BROKER_TOPIC_PROP);
  }

  public String getJmsNotifQueue() { return properties.getProperty(BROKER_NOTIF_QUEUE_PROP); }

  public String getJmsNotifTopic() { return properties.getProperty(BROKER_NOTIF_TOPIC_PROP); }
}
