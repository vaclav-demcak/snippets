package vd.sandbox.spring.amqp;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import org.apache.activemq.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import vd.sandbox.spring.amqp.model.Notification;

@Component
public class AmqpQueueListener {

  private static final Logger LOG = LoggerFactory.getLogger(AmqpQueueListener.class);

//  @JmsListener(destination = "${spring.activemq.server-queue}")
//  public void receiveMessage(Message message) {
//    LOG.info("Received message {}", message);
//  }

  @JmsListener(destination = "${spring.activemq.server-queue}", containerFactory = "queueListenerFactory")
  @SendTo("${spring.activemq.client-queue}")
  public String receiveMessage(Message message) throws JMSException {
    LOG.info("Received message {}", message);
    String conntent = ((TextMessage) message).getText();
    LOG.info("Text Message Content : {}",conntent);
    return "Server response " + conntent;
  }

  @JmsListener(destination = "${spring.activemq.server-queue}", containerFactory = "queueListenerFactory")
//  @SendTo("${spring.activemq.client-queue}")
  public void receiveNotification(Notification message) throws JMSException {
    LOG.info("Received message {}", message);
//    String conntent = ((TextMessage) message).getText();
//    LOG.info("Text Message Content : {}",conntent);
//    return "Server response " + conntent;
  }

//  @JmsListener(destination = "${spring.activemq.server-queue}", containerFactory = "queueListenerFactory")
//  @SendTo("${spring.activemq.client-queue}")
//  public String receiveNotification(Notification message) throws JMSException {
//    LOG.info("Received message {}", message);
//    String conntent = ((TextMessage) message).getText();
//    LOG.info("Text Message Content : {}",conntent);
//    return "Server response " + conntent;
//  }

  @PostConstruct
  public void inform() {
    LOG.info("started ...");
  }
}
