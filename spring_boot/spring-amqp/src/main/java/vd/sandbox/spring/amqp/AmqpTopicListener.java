package vd.sandbox.spring.amqp;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import vd.sandbox.spring.amqp.model.Notification;

@Component
public class AmqpTopicListener {

  private static final Logger LOG = LoggerFactory.getLogger(AmqpTopicListener.class);

  @JmsListener(destination = "${spring.activemq.topic}", containerFactory = "topicListenerFactory")
  public void receiveMessage(Message message) throws JMSException {
    LOG.info("Received message from topic {}", message);
    LOG.info("Received message text: {}", ((TextMessage)message).getText());
  }

  @JmsListener(destination = "${spring.activemq.notif-topic}", containerFactory = "notifTopicListenerFactory")
  public void receiveNotification(Notification message) throws JMSException {
    LOG.info("Received notification from topic {}", message);
//    LOG.info("Received message text: {}", ((TextMessage)message).getText());
  }

  @PostConstruct
  public void inform() {
    LOG.info("started ...");
  }
}
