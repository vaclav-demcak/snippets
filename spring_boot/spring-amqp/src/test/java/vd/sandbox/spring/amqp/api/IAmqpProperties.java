package vd.sandbox.spring.amqp.api;

import java.net.URI;
import java.net.URISyntaxException;

public interface IAmqpProperties {

  URI getJmsUri();

  String getJmsUser();

  String getJmsPwd();

  String getJmsClientQueue();

  String getJmsServerQueue();

  String getJmsTopic();

  String getJmsNotifQueue();

  String getJmsNotifTopic();
}
