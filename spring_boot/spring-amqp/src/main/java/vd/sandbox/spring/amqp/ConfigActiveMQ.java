package vd.sandbox.spring.amqp;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

//https://www.devglan.com/spring-boot/spring-boot-jms-activemq-example

@EnableJms
@Configuration
public class ConfigActiveMQ {

  @Bean
  public ActiveMQConnectionFactory connectionFactory() {
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
    connectionFactory.setBrokerURL(mqBrokerUrl);
    connectionFactory.setPassword(password);
    connectionFactory.setUserName(user);
    return connectionFactory;
  }

  @Bean(name = "jmsQueueTemplate")
  public JmsTemplate jmsQueueTemplate() {
    JmsTemplate template = new JmsTemplate();
    template.setConnectionFactory(connectionFactory());
    return template;
  }

  @Bean(name = "jmsTopicTemplate")
  public JmsTemplate jmsTopicTemplate() {
    JmsTemplate template = new JmsTemplate();
    template.setConnectionFactory(connectionFactory());
    template.setPubSubDomain(true);
    return template;
  }

  @Bean(name = "jmsNotifQueueTemplate")
  public JmsTemplate jmsNotifQueueTemplate() {
    JmsTemplate template = new JmsTemplate();
    template.setConnectionFactory(connectionFactory());
    template.setMessageConverter(jacksonJmsMessageConverter());
    return template;
  }

  @Bean(name = "jmsNotifTopicTemplate")
  public JmsTemplate jmsNotifTopicTemplate() {
    JmsTemplate template = new JmsTemplate();
    template.setConnectionFactory(connectionFactory());
    template.setMessageConverter(jacksonJmsMessageConverter());
    template.setPubSubDomain(true);
    return template;
  }

  @Bean
  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory());
    factory.setConcurrency("1-1");
    return factory;
  }

  public void registerBeans(ConfigurableApplicationContext context) {
    BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(JmsTemplate.class);
    CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();

    builder.addPropertyValue("connectionFactory", cachingConnectionFactory);      // set property value
    DefaultListableBeanFactory factory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
    factory.registerBeanDefinition("jmsTemplateName", builder.getBeanDefinition());
  }

  @Bean
  public JmsListenerContainerFactory<?> topicListenerFactory(
      ConnectionFactory connectionFactory,
      DefaultJmsListenerContainerFactoryConfigurer configurer) {
    LOG.info("ActiveMQ Topic Listener Factory Bean configure ...");
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    // This provides all boot's default to this factory, including the message converter
    configurer.configure(factory, connectionFactory);
    factory.setPubSubDomain(true);
    // You could still override some of Boot's default if necessary.
    return factory;
  }

  @Bean
  public JmsListenerContainerFactory<?> notifTopicListenerFactory(
      ConnectionFactory connectionFactory,
      DefaultJmsListenerContainerFactoryConfigurer configurer) {
    LOG.info("ActiveMQ Topic Listener Factory Bean configure ...");
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    // This provides all boot's default to this factory, including the message converter
    factory.setMessageConverter(jacksonJmsMessageConverter());
    configurer.configure(factory, connectionFactory);
    factory.setPubSubDomain(true);
    // You could still override some of Boot's default if necessary.
    return factory;
  }

  @Bean
  public JmsListenerContainerFactory<?> queueListenerFactory(
      ConnectionFactory connectionFactory,
      DefaultJmsListenerContainerFactoryConfigurer configurer) {
    LOG.info("ActiveMQ Queue Listener Factory Bean configure ...");
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    // This provides all boot's default to this factory, including the message converter
    configurer.configure(factory, connectionFactory);
    return factory;
  }

  @Bean
  public JmsListenerContainerFactory<?> notifQueueListenerFactory(
      ConnectionFactory connectionFactory,
      DefaultJmsListenerContainerFactoryConfigurer configurer) {
    LOG.info("ActiveMQ Queue Listener Factory Bean configure ...");
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    // This provides all boot's default to this factory, including the message converter
    factory.setMessageConverter(jacksonJmsMessageConverter());
    configurer.configure(factory, connectionFactory);
    return factory;
  }

  @Bean // Serialize message content to json using TextMessage
  public MessageConverter jacksonJmsMessageConverter() {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");
    return converter;
  }

  @PostConstruct
  public void inform() {
    LOG.info("ActiveMQ configurer started...");
  }

  @Value("${spring.activemq.broker-url}")
  private String mqBrokerUrl;
  @Value("${spring.activemq.user}")
  private String user;
  @Value("${spring.activemq.password}")
  private String password;
  @Value("${spring.activemq.client-queue}")
  private String clientQueue;
  @Value("${spring.activemq.topic}")
  private String topic;
  @Value("${spring.activemq.notif-queue}")
  private String notifQueue;
  @Value("${spring.activemq.notif-topic}")
  private String notifTopic;

  public String getClientQueue() {
    return clientQueue;
  }

  public String getTopic() {
    return topic;
  }

  public String getNotifTopic() { return notifTopic; }

  public String getNotifQueue() { return notifQueue; }

  private static final Logger LOG = LoggerFactory.getLogger(ConfigActiveMQ.class);
}