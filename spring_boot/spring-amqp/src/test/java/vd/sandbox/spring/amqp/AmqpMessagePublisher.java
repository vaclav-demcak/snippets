package vd.sandbox.spring.amqp;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vd.sandbox.spring.amqp.api.IAmqpMessagePublisher;
import vd.sandbox.spring.amqp.api.IAmqpProperties;

public class AmqpMessagePublisher implements IAmqpMessagePublisher, ExceptionListener {

  private static final Logger LOG = LoggerFactory.getLogger(AmqpMessagePublisher.class);

  private final IAmqpProperties prop;

  public AmqpMessagePublisher(IAmqpProperties prop) {
    LOG.info("initialization ...");
    this.prop = prop;
  }
  
  @Override
  public void sendTextMsgToServerQueue(String msg) throws JMSException {
    LOG.info("sendTextMsgToServerQueue ... {}", msg);
    Connection connection = null;
    Session session = null;
    MessageProducer producer = null;
    try {
      // Make ConnectionFactory if not exist for specific URL
      ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(prop.getJmsUri());
      // Getting JMS connection from the server and starting it
      connection = connectionFactory.createConnection(prop.getJmsUser(), prop.getJmsPwd());
      // add Error listener to catch the problems with sending msg
      connection.setExceptionListener(this::onException);
      connection.start();

      // Creating a non transactional session to send/recive JMS message.
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      // Destination represent here our queue on the JMS server.
      // The queue will be created automatically on the server.
      Destination destination = session.createQueue(prop.getJmsServerQueue());
      //Now create the actual message you want to send
      TextMessage txtMessage = session.createTextMessage();
      txtMessage.setText(msg);

      // Create a MessageProducer from the Session to the Queue
      producer = session.createProducer(destination);
      producer.send(txtMessage);

    } catch (Exception e) {
      LOG.error("Unexpected Exception ", e);
    } finally {
      if (producer != null) {
        producer.close();
      }
      if (session != null) {
        session.close();
      }
      if (connection != null) {
        connection.close();
      }
    }
  }

  @Override
  public void sendTextMsgToTopic(String msg) throws JMSException {
    LOG.info("sendTextMsgToTopic ... {}", msg);
    Connection connection = null;
    Session session = null;
    MessageProducer producer = null;
    try {
      // Make ConnectionFactory if not exist for specific URL
      ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(prop.getJmsUri());
      // Getting JMS connection from the server and starting it
      connection = connectionFactory.createConnection(prop.getJmsUser(), prop.getJmsPwd());
      // add Error listener to catch the problems with sending msg
      connection.setExceptionListener(this::onException);
      connection.start();

      // Creating a non transactional session to send/recive JMS message.
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      // Destination represent here our topic on the JMS server.
      // The topic will be created automatically on the server.
      Destination destination = session.createTopic(prop.getJmsTopic());
      //Now create the actual message you want to send
      TextMessage txtMessage = session.createTextMessage();
      txtMessage.setText(msg);

      // Create a MessageProducer from the Session to the Queue
      producer = session.createProducer(destination);
      producer.send(txtMessage);

    } catch (Exception e) {
      LOG.error("Unexpected Exception ", e);
    } finally {
      if (producer != null) {
        producer.close();
      }
      if (session != null) {
        session.close();
      }
      if (connection != null) {
        connection.close();
      }
    }
  }

  @Override
  public void onException(JMSException exception) {
    LOG.error("Unexpected error from JMS", exception);
  }
}
