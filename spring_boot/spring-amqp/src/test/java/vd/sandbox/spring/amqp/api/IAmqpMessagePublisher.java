package vd.sandbox.spring.amqp.api;

import javax.jms.JMSException;

public interface IAmqpMessagePublisher {

  void sendTextMsgToServerQueue(String msg) throws JMSException;

  void sendTextMsgToTopic(String msg) throws JMSException;
}
