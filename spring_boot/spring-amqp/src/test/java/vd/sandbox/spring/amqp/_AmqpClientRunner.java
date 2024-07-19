package vd.sandbox.spring.amqp;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vd.sandbox.spring.amqp.api.IAmqpProperties;

//https://examples.javacodegeeks.com/enterprise-java/jms/jms-messagelistener-example/
public class _AmqpClientRunner {

  private static final String CHOSE_1 = "1";
  private static final String CHOSE_2 = "2";
  private static final String CHOSE_END = "3";

  private static final String INIT_INFO_MSG = new StringBuilder("Vyberte jednu z moznosti:")
      .append(System.getProperty("line.separator"))
      .append(CHOSE_1).append(" Poslat MESSAGE na JMS Test Server Queue").append(System.getProperty("line.separator"))
      .append(CHOSE_2).append(" Poslat MESSAGE na JMS Test Topic").append(System.getProperty("line.separator"))
      .append(CHOSE_END).append(" ...Ukoncit program ...").append(System.getProperty("line.separator"))
      .toString();

  public static void main(String[] args) throws Exception {
    LOG.info("AMQP client starting ...");
    _AmqpProperties prop = new _AmqpProperties();
    _AmqpClientRunner runner = new _AmqpClientRunner(prop);
    runner.consoleScann(null);
  }

  public _AmqpClientRunner(IAmqpProperties prop) throws Exception {
    LOG.info("initialization ...");
    this.msgPublisher = new AmqpMessagePublisher(prop);
    this.topicListener = new AmqpTopicListener(prop);
    this.queueListener = new AmqpQueueListener(prop);
    this.notifQueueListener = new AmqpNotifQueueListener(prop);
    this.notifTopicListener = new AmqpNotifTopicListener(prop);
  }

  private void consoleScann(final Scanner scanner) {
    Scanner sc;
    if (scanner == null) {
      sc = new Scanner(System.in);
    } else {
      sc = scanner;
    }
    sc.useDelimiter(System.getProperty("line.separator"));
    consoleInitScaner(sc);
  }

  private void consoleInitScaner(Scanner sc) {
    System.out.println(INIT_INFO_MSG);
    switch (sc.nextLine()) {
      case CHOSE_1:
        scanMsgToQueue(sc);
      case CHOSE_2:
        scanMsgToTopic(sc);
      case CHOSE_END:
        System.out.println("Dakujeme ...");
        System.exit(0);
      default:
        System.out.println("Zadany vstup nebol rozpoznany, pouzi len cislo");
        consoleScann(sc);
    }
  }

  private void scanMsgToQueue(Scanner sc) {
    System.out.println("Napis Message ktora sa ma poslat na server Queue");
    String msg = sc.nextLine();
    try {
      msgPublisher.sendTextMsgToServerQueue(msg);
    } catch (Exception e) {
      LOG.error("Unexpected Error ", e);
    }
    consoleScann(sc);
  }

  private void scanMsgToTopic(Scanner sc) {
    System.out.println("Napis Message ktora sa ma poslat na Topic");
    String msg = sc.nextLine();
    try {
      msgPublisher.sendTextMsgToTopic(msg);
    } catch (Exception e) {
      LOG.error("Unexpected Error ", e);
    }
    consoleScann(sc);
  }

  public final void finalize() {
    if (queueListener != null) {
      try { queueListener.close();
      } catch (Exception e) { LOG.error("Unexpected error ", e); }
    }
    if (topicListener != null) {
      try { topicListener.close();
      } catch (Exception e) { LOG.error("Unexpected error ",e); }
    }
    if (notifQueueListener != null) {
      try { notifQueueListener.close();
      } catch (Exception e) { LOG.error("Unexpected error ",e); }
    }
    if (notifTopicListener != null) {
      try { notifTopicListener.close();
      } catch (Exception e) { LOG.error("Unexpected error ",e); }
    }
  }

  private AmqpQueueListener queueListener;
  private AmqpTopicListener topicListener;
  private AmqpNotifQueueListener notifQueueListener;
  private AmqpNotifTopicListener notifTopicListener;
  private AmqpMessagePublisher msgPublisher;

  private static final Logger LOG = LoggerFactory.getLogger(_AmqpClientRunner.class);
}
