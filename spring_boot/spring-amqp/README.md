
[_metadata_:author]:- "Václav Demčák"
[_metadata_:title]:- "Spring Swagger"
[_metadata_:date]:- "03/06/2020 14:32 PM" 

# Java Message Service JMS
Cieľom tutorálu je predstaviť programátorovi [JMS](https://en.wikipedia.org/wiki/Java_Message_Service) API.
Uviesť ho do problematiky a previesť ho základnými konštrukciami a použitím v praxy.

## Obsah
1. [JMS](#JMS)
2. [Messaging](#Messaging)
3. [Topics vs Queues](#Topics-vs-Queues)
    1. [JMS Topics](#JMS-Topics)
    2. [JMS Queues](#JMS-Queues) 
4. [Prvotné spustenie](#Prvotné-spustenie)
    1. [ActiveMQ](#ActiveMQ)
    2. [SpringBoot](#SpringBoot)
    3. [Java Client](#Java-Client)
5. [Konfigurácia SpringBoot](#Konfigurácia-SpringBoot)
    1. [ActiveMQ SpringBoot Konfigurácia](#ActiveMQ-SpringBoot-Konfigurácia)
    2. [ActiveMQ Connector Konfigurácia](#ActiveMQ-Connector-Konfigurácia)
    3. [ActiveMQ ListenerConnector Konfigurácia](#ActiveMQ-ListenerConnector-Konfigurácia)
    4. [ActiveMQ ProducerConnector Konfigurácia](#ActiveMQ-ProducerConnector-Konfigurácia)
6. [Implementácia na Servery](#Implementácia-na-Servery)
    1. [AMQP Listener](#AMQP-Listener)
    2. [AMQP Publishers](#AMQP-Publishers)
7. [Java Klient](#Java-Klient)
    1. [AMQP Listeners](#AMQP-Listeners)
    2. [AMQP Producers](#AMQP-Producers)
8. [Literatúra](#Literatúra)

## JMS
[JMS](https://en.wikipedia.org/wiki/Java_Message_Service) API je Java [message-oriented middleware](https://en.wikipedia.org/wiki/Message-oriented_middleware)
slúžiaci na distribúciu správ medzi dvoma alebo viacerými klientami. Prostredníctvom systému založenom
na posielaní správ, je možné prenášať údaje rýchlo, spoľahlivo, často a asynchrónne. Získame tak flexibilný
systém, ktorý umožňuje zdieľať údaje aj medzi aplikáciami, ktoré sú implementované rozdielnymi technológiami.
Prostredníctvom správ je rovnako možné zdieľať aj istú funkcionalitu.

## Messaging
Tzv. **Messaging** je zakladom [*Message Driven Architecture*](https://blog.sapiensworks.com/post/2013/04/19/Message-Driven-Architecture-Explained-Basics.aspx)
a rovnako tak aj [*Event Driven Architecture*](https://en.wikipedia.org/wiki/Event-driven_architecture)
[*Software architecture*](https://en.wikipedia.org/wiki/Software_architecture) so zameraním na servisy
[*Service-Oriented architecture SOA*](https://en.wikipedia.org/wiki/Service-oriented_architecture) sa
v dňešných dňoch sa architektúry systémov uberajú smerom [**Event-driven SOA**](https://en.wikipedia.org/wiki/Event-driven_SOA)
a [**Reactive Architecture**](https://android.jlelse.eu/reactive-architecture-7baa4ec651c4), takže
zvládnutie JMS prístupov je už viac než nevyhnutné.

## Topics vs Queues
Pre prácu s JMS je pri návrhu potrebné uvedomiť si základný rozdiel medzi **JMS Queue** a **JMS Topic**.
### JMS Topics
Topic implementuje *publish* a *subscribe* schématiku v rámci JMS. Tj. *publisher* (tiež sa používa
termín *producer* resp. *poskytovateľ*) je zodpovedný za odoslanie správy (notifikácie) do JMS.
Subscriber (tiež sa používa termín *receiver* alebo *konzumer*) je odpovedný za jej prijatie.
Teda hovoríme o tom, že JEDEN poskytovateľ má NULA alebo VEĽA naslucháčov, ktorí dostanú kópiu správy.
A kópiu správy dostane len naslucháč s aktívnym pripojením v čase odoslania správy. JMS je zodpovedná
za distribúciu správ všetkým kto má o danú správu záujem. Opísané správanie odzrkadľuje
[**Broadcast**](https://sk.wikipedia.org/wiki/Broadcast) správanie známe zo sveta počítačových sietí.
V zásade platí pravidlo, že poskytovateľ nepotrebuje vedieť o tom, kto a prečo jeho správy potrebuje 
a teda pri návrhu nemusíme uvažovať o spätnej väzbe od konzumerov správ.
> Príklad schémantiky: *tlač dokumentu vybraným tlačiarňam* - ak užívateľ v programe pošle dokument
> na tlač, tak program nepotrebuje vedieť, že je dokument vytlačený. Ak sa tlač neuskutoční, alebo
> sa vyskytnú iné problémy, užívateľ odošle požiadavku na tlač dokumentu znovu.
### JMS Queues
Queue alebo aj zásobník implementuje tzv. [*load balancer*](https://sk.wikipedia.org/wiki/Vyrovn%C3%A1vanie_z%C3%A1%C5%A5a%C5%BEe)
schématiku v rámci JMS. Každá jedna správa je odoslaná presne jednému konzumerovi. Ak nieje konzumer
aktívny v čase odoslania správy, správa čaká v poradovníku až kým sa konzumer nepripojí. Ak je správa
konzumerovi doručená a on nepotvrdí doručenie, bude doručená inému konzumerovi. Vo fronte môže byť
veľa konzumerov a JMS vyberá, ktorému bude tá ktorá správa doručená. Opísané správanie zrkadlí
[**P2P**](https://sk.wikipedia.org/wiki/Sie%C5%A5_so_vz%C3%A1jomn%C3%BDm_spr%C3%ADstup%C5%88ovan%C3%ADm)
správanie známe zo sveta sieťových pripojení. V zásade, doručenie každej správy musí byť konzumerom
riadne potvredené a správa je následne považovaná za doručenú a je vymazaná z JMS.
> Pre pochopenie schématiky môžeme uvažovať JMS Queue komunikáciu ako veľmi podobnú klasickému
> request-response synchrónnu komunikaciu medzi dotazujúcim sa systémom a odpovedajúcim systémom.


## Prvotné spustenie
Projekt obsahuje časti iných technológii ktore sú obsahom iných tutoriálov v rôznej urovni zložitosti.
Pre úplné pochopenie obsahu projektu odporúčame prejsť aj nasledovne tutorialy:
* SpringBoot (todo: add link) - konfigurácia a vystavnie Swagger UI servera SpringBoot
* Docker Compose (todo: add link) - priprava lokalneho prostredia pre plynulý vývoj (ActiveMQ)

Skôr ako pristúpime k predstaveniu jednotlivých blokov projektu, maly by sme si projekt spustiť
a overiť jeho fuknčnosť. Predpokladom k úspešnému spusteniu projektu je inštalovaný:
* [Docker](https://docs.docker.com/get-docker/) pre inicializáciu okolia (MySQL DB)
* [Gradle](https://gradle.org/install/) pre vlastné spustenie projektu
* Java pre spustenie klienta

### ActiveMQ
[ActiveMQ](http://activemq.apache.org/) je jednou z implementácii AMQP rozhrania. Alternatívy sú ([RabbitMQ](https://www.rabbitmq.com/), [Kafka](https://kafka.apache.org/) ...)
Ulohou je zabezpečiť JMS komunikáciou medzi časťami komplexnejšej systémovej skladby viacerých applikácii.
Inicializáciu zabezpečuje skript *docker-compose.yml*.
```shell script
docker-compose up
```
> Konfiguráciou ActiveMQ sa zatiaľ zaoberať nebudeme. Pre testovanie je vhodne pozrieť sa na webove
> rozhranie ActiveMQ [http://localhost:8161/admin](http://localhost:8161/admin).
### SpringBoot
Web servisy pre odosielanie správ na JMS je implementované v prostredí SpringBoot-u. Po spustení triedy
*vd.sandbox.spring.amqp._SampleSpringBootRunner* alebo spustení SpringBoot-u za pomoci *gradle*
``` shell script
./gradlew bootRun
```
budeme mať webové rozhranie na odosielanie JMS správ, ktoré môžeme sledovať v ActiveMQ web rozhrani.
> SpringBoot vystavý svoje API a Swagger Web Rozhranie na URL [http://localhost:8080](http://localhost:8080).
### Java Client
Pre pochopenie komunikácie sme pridali aj Java Klient, ktorý posiela a konzumuje správy z JMS.
**vd.sandbox.spring.amqp._AmqpClientRunner* v Test časti projektu.
```java
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
  // ...
}
```
> Ide o konzolovú java aplikáciu, ktorú ovládame za pomoci výberov z menu v konzolovom prostredi.
> Aplikácia ma aktívne JMS Listenery a umožňuje zasielanie správ na JMS. Prijaté správy o5 vypíše
> na konzolu.

## Konfigurácia SpringBoot
**SpringBoot** je našim kontajnerom. Takže potrebujeme pluginy springBoot-u. Tu sme vypísali len nutné
závyslosti pre ActiveMQ klienta. Projekt zahŕňa aj Swagger a REST web services.
> ${project_dir}/build.gradle
``` groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '2.2.2.RELEASE'
    id 'io.spring.dependency-management' version '1.0.9.RELEASE'
}
...
dependencies {
    compile("org.springframework.boot:spring-boot-starter-activemq")
    compile("org.apache.activemq:activemq-broker")
    compile("com.fasterxml.jackson.core:jackson-databind")
}
```

### ActiveMQ SpringBoot Konfigurácia
SpringBoot má vlasné *properties* pre JMS a ActiveMQ [viac tu](https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-application-properties.html#integration-properties)
Teda prvý krok pri konfigurácii nás zavedie do súboru
> ${project_dir}/src/main/resources/application.yml
```yaml
spring:
  jms:
    pub-sub-domain: true
    cache:
      enabled: false # see: https://github.com/spring-projects/spring-boot/issues/19565
  activemq:
    broker-url: tcp://localhost:61616
    user: admi
    password: admin
```
> SpringBoot ma tzv. AutoConfiguration pre najčastejšie prípady konfigurácie.
> Takže, ak nepotrebujeme obmieňať default, dodatočnú konfiguráciou neodporúčame.

**spring.jms.pob-sub-domain** - defaultne je SpringBoot konfigurácia nastavená na Queue,
ak chceme pripojenie na Topic, táto property musí mať nastavenú hodnotu **true**.

**spring.jms.cache.enabled** - SpringBoot ma defaultne nastavenú *cache* inštanciu ActiveMQConnetionFactory
a ta je pre túto verziu očividne na prd. Treba teda vypnuť cahe tj. hodnota musí byť **false**.

**spring.activemq.broker-url** - property definuje, kde nám JMS načúva a kde sa budeme pripájať.

**spring.activemq.user** - na pripojenie nám treba vedieť užívateľa

**spring.activemq.password** - na pripojenie nám treba aj užívateľské heslo

### ActiveMQ Connector Konfigurácia    
Keďže sme postavili príklad tak, aby sme mohli demonštrovať zasielanie aj príjmanie správ tak
z Queue ako aj z Topic-u, museli sme pridať vlastnú konfiguráciu pre ActiveMQ.
> vd.sandbox.spring.amqp.ConfigActiveMQ
```java
@EnableJms
@Configuration
public class ConfigActiveMQ {

  @Value("${spring.activemq.broker-url}")
  private String mqBrokerUrl;
  @Value("${spring.activemq.user}")
  private String user;
  @Value("${spring.activemq.password}")
  private String password;

  @Bean
  public ActiveMQConnectionFactory connectionFactory() {
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
    connectionFactory.setBrokerURL(mqBrokerUrl);
    connectionFactory.setPassword(password);
    connectionFactory.setUserName(user);
    return connectionFactory;
  }
}
``` 
> Konfigurácia ConnectionFactory - Takto sme deklarovali vlastnú ActiveMQ ConnectionFactory
> pre všetky pripojenia (tak listenerov ako providerov). Keď ju robí spring v autoConfig mode,
> narážali sme na ombedzenia definované pri jej vzniku, keďže sa spring snažil urobiť ju buď to
> pre Queue alebo pre Topic. Takto mame nad ňou plnú kontrolu a vieme dodefinovať vlasné
> ConnectionFactory pre Queue aj pre Topic dodatočne.
### ActiveMQ ListenerConnector Konfigurácia
Pre listenery sa registrujú pri štarte aplikácie a majú svoj ListenerContainerFactory vychádzajúci
zo všeobecného, ktorý sme predstavili vyžšie.
> vd.sandbox.spring.amqp.ConfigActiveMQ
```java
  @Bean
  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory());
    factory.setConcurrency("1-1");
    return factory;
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
  public JmsListenerContainerFactory<?> queueListenerFactory(
      ConnectionFactory connectionFactory,
      DefaultJmsListenerContainerFactoryConfigurer configurer) {
    LOG.info("ActiveMQ Queue Listener Factory Bean configure ...");
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    // This provides all boot's default to this factory, including the message converter
    configurer.configure(factory, connectionFactory);
    return factory;
  }
```
> "Zázračný prepínač" **factory.setPubSubDomain(true);** je nutné pridať pre Topic. Inak
> niesme schopný na topic eventy počúvať. Keďže máme oba listenery tak Topic ako Queue,
> treba nám definovať dva *ListenerContainerFactory*.
### ActiveMQ ProducerConnector Konfigurácia
Pre producery sa registrujú pri štarte aplikácie *JMSTemplate* slúžiace na odosielanie správ na JMS.
> vd.sandbox.spring.amqp.ConfigActiveMQ
```java
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
```
> O5 "zázračný prepínač" **template.setPubSubDomain(true);** je nutné pridať pre Topic. Inak
> niesme schopný na topic eventy posielať. Keďže máme oba producer-y tak Topic ako Queue,
> treba nám definovať dva *JmsTemplate*.

## Implementácia na Servery
Na tomto mieste už máme všetko nakonfigurované a treba nám prejsť k implementačnej časti.
### AMQP Listener
Spring poskytuje veľmi jednoduchú techniku implementácie JMS listenerov za pomoci anotácie:

**@JmsListener** - tj. metóda označená anotáciou bude počúvať na správy z JMS
- destination : meno Queue / Topic-u na ktorý počúvame
- containerFactory : meno metódy poskytujúcej *JmsListenerContainerFactory*
> vd.sandbox.spring.amqp.AmqpQueueListener
```java
@Component
public class AmqpQueueListener {

  private static final Logger LOG = LoggerFactory.getLogger(AmqpQueueListener.class);

  @JmsListener(destination = "${spring.activemq.server-queue}", containerFactory = "queueListenerFactory")
  public void receiveMessage(Message message) throws JMSException {
    LOG.info("Received message from queue {}", message);
    String conntent = ((TextMessage) message).getText();
    LOG.info("Text Message Content : {}",conntent);
  }
}
```
> vd.sandbox.spring.amqp.AmqpTopicListener
```java
@Component
public class AmqpTopicListener {

  private static final Logger LOG = LoggerFactory.getLogger(AmqpTopicListener.class);

  @JmsListener(destination = "${spring.activemq.topic}", containerFactory = "topicListenerFactory")
  public void receiveMessage(Message message) throws JMSException {
    LOG.info("Received message from topic {}", message);
    LOG.info("Received message text: {}", ((TextMessage)message).getText());
  }
}
```
Pre reťazenie Request/Response komunikácie s JMS je veľmi užitočná anotácia:

**@SendTo** - Metóda príjemcu správy môže mať aj okamžitý výstup preposlaný na špecifické JMS Queue/Topic
> vd.sandbox.spring.amqp.AmqpQueueListener
```java
@Component
public class AmqpQueueListener {

  private static final Logger LOG = LoggerFactory.getLogger(AmqpQueueListener.class);

  @JmsListener(destination = "${spring.activemq.server-queue}", containerFactory = "queueListenerFactory")
  @SendTo("${spring.activemq.client-queue}")
  public String receiveMessage(Message message) throws JMSException {
    LOG.info("Received message {}", message);
    String conntent = ((TextMessage) message).getText();
    LOG.info("Text Message Content : {}",conntent);
    return "Server response " + conntent;
  }
}
```
### AMQP Publishers
Na odosielanie správ do JMS sme si už vytvorili *Bean-y* typu *JmsTemplate* v triede *ConfigActiveMQ*.
Takže metodika odosielania pozostáva len z **bean injection** a prípravy správy na odoslanie.
> vd.sandbox.spring.amqp.AmqpMessagePublisher
```java
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
    jmsQueueTemplate.convertAndSend(configAmqp.getClientQueue(), message);
  }

  public void sendNotificationToTopic(Notification message) {
    LOG.info("sending to topic notification={}", message);
    jmsTopicTemplate.convertAndSend(configAmqp.getTopic(), message);
  }
}
``` 

## Java Klient
SpringBoot server aj ActiveMQ už máme. Dokonca je možné ich aj testovať. Avšak pre úplné pochopenie
komunikácie, nám stále chýba kooperujúci systém. Pre tento demonštratívny príklad sme zvolili základného
java klienta implementujúceho len jednoduché aplikačné rozhranie príkazového riadku.
Jedinou nutnou podmienkou pre Java klienta je implementácia balíčka:
```groovy
compile("org.apache.activemq:activemq-broker")
```
### AMQP Properties
Na to aby klient vedel kde sa môže pripojiť, musí mať zadefinované property **URL**, **USER** a **PWD**.
Zahrnuli sme ich do súboru *activemq.properties*
> ${project_dir}/src/test/resources/activemq.properties
```properties
amqp.broker.uri=tcp://localhost:61616
amqp.broker.user=admin
amqp.broker.pwd=admin
amqp.broker.client-queue=test_client_queue
amqp.broker.server-queue=test_server_queue
amqp.broker.topic=test_topic
``` 
Načítanie a správu týchto properties realizuje trieda *vd.sandbox.spring.amqp._AmqpProperties*.
### AMQP Listeners
Pri spustení programu, hneď po načítaní *Properties* realizujeme registráciu listenerov na Queue aj Topic.
JMS listenery zvyčajne rozširujú interface-y:
* **javax.jms.MessageListener # onMessage(Message message)**
* **javax.jms.ExceptionListener # onException(JMSException exception)**

> vd.sandbox.spring.amqp.AmqpTopicListener
``` java
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
```
> vd.sandbox.spring.amqp.AmqpQueueListener
``` java
public class AmqpQueueListener implements MessageListener, ExceptionListener, Closeable {

  private static final Logger LOG = LoggerFactory.getLogger(AmqpTopicListener.class);

  private final IAmqpProperties properties;
  private final Connection connection;
  private final Session session;
  private final MessageConsumer messageConsumer;

  public AmqpQueueListener(IAmqpProperties prop) throws JMSException {
    LOG.info("initialization ...");
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
    Destination destination = session.createQueue(prop.getJmsClientQueue());
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
    LOG.info("closing ...");
    messageConsumer.close();
    session.close();
    connection.close();
  }
}
```
> Kód vieme generalizovať a pre jednotlivé implementácie zjednodušiť spoločným interface-om alebo
> abstraktnou triedou. Stratíme ale komlexitu riešenia. Teda v príklade sa tým zaoberať nebudeme.
### AMQP Producers
Posielať aktívne správy do JMS nieje tiež žiadna raketová veda. Treba nám len klienta a starať sa
o jeho korektný životný cyklus. Nasledovný príklad prezentuje celé odosielanie správ do JMS z klienta.
> vd.sandbox.spring.amqp.AmqpMessagePublisher
``` java
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
```

## JMS posielanie objektov
Keďže základná štruktúra JMS dokáže pracovať len s *message* je potrebná serializácia a deserializácia
zasielaných objektov na JMS. *javax.jms* v zaklade podporuje *java.io.Serializable* posielanie objektov.
Takto serializované objekty sú nám na honvno. Nie sme schopný rýchlo a jednoducho pozrieť sa na ich obsah
a pri hľadaní chýb dátového charakteru sme v podstate hluchý a slepý. 

> [**JSON**](https://en.wikipedia.org/wiki/JSON) formát dátovej štruktúry poskytuje **human readable form** kdekoľvek ho použijeme.

### JMS Object server part
[OpenAPI](https://www.openapis.org/) nám generuje už parsovateľné objekty pomocou JSON-u, tak prečo
toho rovno nevyužiť. Pri jednoduchej definicii EndPointov (ktoré vo výsledku vôbec nemusíme vystaviť)
nám OpenAPI generátor vyrobí všetko čo potrebujeme a stačí nám len pridať tzv. *MessageConverter*.
> vd.sandbox.spring.amqp.ConfigActiveMQ
```java
...
  @Bean // Serialize message content to json using TextMessage
  public MessageConverter jacksonJmsMessageConverter() {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");
    return converter;
  }
...
```
Ten teraz musíme dolinkovať do želaných štruktúr :
* Pre Listenery ho musíme pridať do *JmsListenerContainerFactory*
> vd.sandbox.spring.amqp.ConfigActiveMQ
```java
...
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
...
```
* Pre providery ho musíme pridať do *JmsTemplate* 
> vd.sandbox.spring.amqp.ConfigActiveMQ
```java
...
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
...
```
Teda pre Listener bude použitie nasledovné
> vd.sandbox.spring.amqp.AmqpTopicListener
```java
...
  @JmsListener(destination = "${spring.activemq.notif-topic}", containerFactory = "notifTopicListenerFactory")
  public void receiveNotification(Notification message) throws JMSException {
    LOG.info("Received notification from topic {}", message);
  }
...
```
Rovnako jednoduché je to aj pre queue, ale implementáciu už necháme na čitateľa.

U producer-a je odosielanie len otázkou použitia správneho *JmsTemplate* objektu.
> vd.sandbox.spring.amqp.AmqpMessagePublisher
```java
@Component
public class AmqpMessagePublisher {

  private static final Logger LOG = LoggerFactory.getLogger(AmqpMessagePublisher.class);

  @Autowired
  private ConfigActiveMQ configAmqp;
  @Autowired
  @Qualifier("jmsNotifQueueTemplate")
  private JmsTemplate jmsNotifQueueTemplate;
  @Autowired
  @Qualifier("jmsNotifTopicTemplate")
  private JmsTemplate jmsNotifTopicTemplate;

  public void sendNotificationToClient(Notification message) {
    LOG.info("sending to client queue notification={}", message);
    jmsNotifQueueTemplate.convertAndSend(configAmqp.getNotifQueue(), message);
  }

  public void sendNotificationToTopic(Notification message) {
    LOG.info("sending to topic notification={}", message);
    jmsNotifTopicTemplate.convertAndSend(configAmqp.getNotifTopic(), message);
  }
}
``` 

### JMS Object client part
Na strane Java klienta, už musíme len *deserialize-ovať* prichádzajúcu *Message* na náš objekt.
>vd.sandbox.spring.amqp.AmqpNotifQueueListener
```java
...
  @Override
  public void onMessage(Message message) {
    LOG.info("Message from JMS {}", message);
    String content = null;
    try {
      content = ((TextMessage)message).getText();
      Notification notif = new ObjectMapper()
          .readerFor(Notification.class)
          .readValue(content);
      LOG.info("Notification object {}", notif);
    } catch (JMSException | JsonProcessingException e) {
      LOG.error("Unexpected JMS Exception ", e);
    }
    LOG.info("Message content {}", content);
    // TODO : add necessary body
  }
...
```
Tým je celá mágia JMS opísaná. Len pre doplnenie, odporúčame poslať Notifikačný Objekt do Queue cez
Swagger rozhranie tohto príkladu tak, aby client nebol spustený. Sprava bude čakať na konzumera a my
si ju môžeme prečítať. cez web konzolu [ActiveMQ notif_queue](http://localhost:8161/admin/browse.jsp?JMSDestination=notif_queue)
 
## Literatúra
* [Spring JMS](https://spring.io/guides/gs/messaging-jms/)
* [Spring ActiveMQ sample](https://www.devglan.com/spring-boot/spring-boot-jms-activemq-example)
* [Zakladna ActiveMQ Message Properties](http://activemq.apache.org/activemq-message-properties)
* [Topics vs Queues](https://stackoverflow.com/questions/5576415/jms-topic-vs-queues)
* [Spring AMQP](https://github.com/eugenp/tutorials/tree/master/spring-amqp)
* [Spring ActiveMQ example](https://www.journaldev.com/12743/spring-activemq-example)
* [Event Driven Microservice and ActiveMQ](https://itnext.io/event-driven-microservices-with-spring-boot-and-activemq-5ef709928482)
* [Spring JMS ActiveMQ](https://codenotfound.com/spring-jms-activemq-example.html)
* [Spring JMS Tutorial](https://dzone.com/articles/spring-boot-jms-activemq-example-spring-boot-tutor)






