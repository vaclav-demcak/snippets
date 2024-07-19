package vd.sandbox.spring.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import vd.sandbox.spring.amqp.model.Notification;

@Component
public class AmqpMessagePublisher {

  private static final Logger LOG = LoggerFactory.getLogger(AmqpMessagePublisher.class);

  @Autowired
  private ConfigActiveMQ configAmqp;
  @Autowired
  @Qualifier("jmsQueueTemplate")
  private JmsTemplate jmsQueueTemplate;
  @Autowired
  @Qualifier("jmsTopicTemplate")
  private JmsTemplate jmsTopicTemplate;
  @Autowired
  @Qualifier("jmsNotifQueueTemplate")
  private JmsTemplate jmsNotifQueueTemplate;
  @Autowired
  @Qualifier("jmsNotifTopicTemplate")
  private JmsTemplate jmsNotifTopicTemplate;

  public void sendMsgToClient(String message) {
    LOG.info("sending to client queue message={}", message);
    jmsQueueTemplate.convertAndSend(configAmqp.getClientQueue(), message);
  }

  public void sendMsgToTopic(String message) {
    LOG.info("sending to topic message={}", message);
    jmsTopicTemplate.convertAndSend(configAmqp.getTopic(), message);
  }

  public void sendNotificationToClient(Notification message) {
    LOG.info("sending to client queue notification={}", message);
    jmsNotifQueueTemplate.convertAndSend(configAmqp.getNotifQueue(), message);
  }

  public void sendNotificationToTopic(Notification message) {
    LOG.info("sending to topic notification={}", message);
    jmsNotifTopicTemplate.convertAndSend(configAmqp.getNotifTopic(), message);
  }
}
