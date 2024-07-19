package vd.sandbox.spring.amqp;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.Closeable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vd.sandbox.spring.amqp.api.IAmqpProperties;

public class AmqpTopicListener implements MessageListener, ExceptionListener, Closeable {

  private static final Logger LOG = LoggerFactory.getLogger(AmqpTopicListener.class);

  private final IAmqpProperties properties;
  private final Connection connection;
  private final Session session;
  private final MessageConsumer messageConsumer;

  public AmqpTopicListener(IAmqpProperties prop) throws JMSException {
    LOG.info("initialization");
    this.properties = prop;

    // Make ConnectionFactory if not exist for specific URL
    ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(prop.getJmsUri());
    // Getting JMS connection from the server and starting it
    connection = connectionFactory.createConnection(prop.getJmsUser(), prop.getJmsPwd());
    connection.setExceptionListener(this::onException);
    connection.start();
    // Creating a non transactional session to send/recive JMS message.
    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    // Destination represent here our topic on the JMS server.
    // The queue will be created automatically on the server.
    Destination destination = session.createTopic(prop.getJmsTopic());
    // Now we need to create Consumer
    messageConsumer = session.createConsumer(destination);
    // so the last think is "register listener for messages"
    messageConsumer.setMessageListener(this::onMessage);
  }

  @Override
  public void onMessage(Message message) {
    LOG.info("Message from JMS {}", message);
    String content = null;
    try {
      content = ((TextMessage)message).getText();
    } catch (JMSException e) {
      LOG.error("Unexpected JMS Exception ", e);
    }
    LOG.info("Message content {}", content);
    // TODO : add necessary body
  }

  @Override
  public void onException(JMSException exception) {
    LOG.error("Unexpected JMS Exception ", exception);
    // TODO : add necessary body
  }

  @Override
  public void close() throws JMSException {
      messageConsumer.close();
      session.close();
      connection.close();
  }
}
