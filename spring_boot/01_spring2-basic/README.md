
[_metadata_:author]:- "Václav Demčák"
[_metadata_:title]:- "Spring Basic"
[_metadata_:date]:- "05/19/2020 19:12 PM" 

# Spring Boot Basic
Cieľom tutoriálu je previesť programátora základnými úplným zákadom [SpringBoot](https://spring.io/projects/spring-boot)

## Obsah
1. [Prvotné spustenie](#Prvotné-spustenie)
2. [Konfigurácia závyslostí](#Konfigurácia-závyslostí)
    1. [Plugins](#Plugins)
    2. [Depenencies](#Depenencies)
3. [Konfigurácia SpringBoot Runner-u](#Konfigurácia-SpringBoot-Runner-u)
4. [Logger](#Logger)
    1. [Logback](#Logback)
    2. [Log4j2](#Log4j2)
5. [Scheduler](#Scheduler)
6. [SpringBoot WAR](#SpringBoot-WAR)
7. [Literatúra](#Literatúra)

## Prvotné spustenie
Projekt obsahuje len SpringBoot server a nepotrebuje žiadne dodatočné vedomosti.
Nutným predpokladom pre úspešné spustenie je mať lokálne nainštalovaný:
* [Gradle](https://gradle.org/install/) pre vlastné spustenie projektu

Tu je vhodne podotknúť, že projekt je koncipovaný pre gradle ver. 6.1. Nato aby sme vedeli zaručiť
konkretnu verziu *Gradle* na cieľovom enviromente, nakonfigurovali sme si gradle.
> ${project_dir}/gradle/wrapper/gradle-wrapper.properties
``` properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-6.1-all.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```
Z tohto dôvodu nemusí fungovať prikaz *gradle build*. Tento príkaz pracuje s lokálne nainštalovanou
verziou gradle. Avšak nasledovný príkaz kontroluje nami definovanú verziu a v prípade že nesedí,
zabezpečí jej stiahnutie a spúšťanie skriptu v tej konkretnej verzii. Teda projekt spúšťame v cmd
za pomoci príkazu *gradlew* (tj. Gradle wrapper). **Gradle Wrapper** jar je súčasťou distribúcie
a rovnako sa nachádza v adresary *${project_dir}/gradle/wrapper/*.
##### Gradle
``` shell script
./gradlew bootRun
```
Po úsešnom naštartovaní servera, otvoríme browser a možeme sa šantiť na Swagger GUI. [http://localhost:8080/](http://localhost:8080/)

## Konfigurácia závyslostí
**SpringBoot** je našim kontajnerom. Takže potrebujeme pluginy springBoot-u.
> ${project_dir}/build.gradle
``` groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '2.2.2.RELEASE'
    id 'io.spring.dependency-management' version '1.0.9.RELEASE'
}
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation('org.springframework.boot:spring-boot-starter-test') {
        exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
    }
}
```
### Plugins
**[java](https://docs.gradle.org/current/userguide/java_plugin.html)** plugin pre kompiláciu java kódu.
**[org.springframework.boot](https://plugins.gradle.org/plugin/org.springframework.boot)** je základný
plugin *spring-framework-u*. Jeho úlohou je príprava definovaného kontajnera (tj. scan implementácie
a nutných závyslostí) a sprístupnenie operácii skriptu **bootRun**.
**[dependency-management](https://plugins.gradle.org/plugin/io.spring.dependency-management)** plugin
zabezpečuje správne verzie integračných súčastí spring frameworku.
### Depenencies
**spring-boot-starter-web** týto sme springframeworku povedali, že chceme používať spring-boot aj
so servletmi (tj. web rozhranim). 

Ďalšiu **SpringBoot** configuráciu budeme realizovať za pomoci [YAML](https://sk.wikipedia.org/wiki/YAML)
konfiguračného súboru. Spring defaultne podporuje len Property konfiguračný súbor. Teda na spravnu
interpretáciu konfigurácie je nutné pridať knižnicu na parsovanie yaml resp. yml konfuračného súboru.

## Konfigurácia SpringBoot Runner-u
**SpringBoot Runner** je *terminus technikus* označujúci java triedu obsahujúcu dve nutné súčasti.
1. anotáciu **@SpringBootApplication** označujúcu, že sa jedna o zavadzač SpringBoot aplikácie
2. java metódu **main** tj. všeobecnú metódu pre spustenie java programu
> vd.sandbox.spring.swagger.SampleSpringBootRunner
``` java
@SpringBootApplication
public class SampleSpringBootRunner {

  public static void main(String[] args) throws Exception {
    new SpringApplication(SampleSpringBootRunner.class).run(args);
  }
}
```
Kompletnú konfiguráciu môžeme zapísať aj do *Runner-a* , avšak tento prístup je ťažkopádny a treba
sa mu vyhýbať. Lepší a prehľadnejší prístup je použitie externého konfiguračného súboru.

> ${project_dir}/src/main/resources/application.yml
``` yaml
 server:
   port: 8080
```
respektíve

> ${project_dir}/src/main/resources/application.properties
``` properties
server.port = 8080
```
Celú špecifikáciu SpringBoot nastavení pre *AutoConfiguration* nájdete [TU](https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-application-properties.html)

> Konfiguráciou sa ale v tomto jednoduchom projekte zaoberať nebudeme. Našim cieľom je oboznámiť sa
> s úplnými základmi SpringBoot-u a vytvoriť cvičný projekt pre developerov na výučbu a prototypovanie
> riešení, či pochopenie súvyslostí.

### AutoConfig
SpringBoot poskytuje možnosť [AutoConfiguration](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/#using-boot-auto-configuration),
čo v praxy znamená, že na základe definovaných properties, pri spustení vytvorí a zavedie všetky
potrebné referenčné triedy z konfiguráku. Je to veľmi pohodlné hlavne keď nepotrebujeme mať úplnú
kontrolu nad inštanciami jednotlivých *funkčných* tried (bean-ov).

## Logger
Logovanie je základným nástrojom komunikácie a overovania možných stavov pri serverových riešeniach.
Je teda nevyhnutné mať správne nakonfigurovaný **Logger**. SpringBoot poskytuje základné rozhranie
**Simple Logging Facade for Java ([SLF4J](http://www.slf4j.org/))** ktorý definuje základné API pre logging.

> Vymýšľať akýkoľvek iný prístup k logovaniu je **HOLÝ NEZMYSEL**. Najrozšírenejšie Logging frameworky
> toto rozhranie podporujú a výmena je len otázkou konfigurácie. Akékoľvek ine Logging API je len
> vymýšľanie kolesa a zaručene nebude dosahovať kvalít existujúcich frameworkov.

Teda MyClass Logger by mal vypadať nasledovne
```java
private static final Logger LOG = LoggerFactory.getLogger(MyClass.class);
```
V plnom znení teda:
> vd.sandbox.spring.LoggerComponent
```java
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggerComponent {

  private static final Logger LOG = LoggerFactory.getLogger(LoggerComponent.class);

  @PostConstruct
  public void postInit() {
    LOG.trace("TRACE log message");
    LOG.debug("DEBUG log message");
    LOG.info("INFO log message");
    LOG.warn("WARN log message");
    LOG.error("ERROR log message");
    additionalLoggingSamples();
  }

  private void additionalLoggingSamples() {
    String param = "SomePram";
    Integer intParam = 5;
    LOG.info("INFO log message with param : {}", param);
    LOG.info("INFO log message with intParam: {} and StringParam {}", intParam, param);
    Exception e = new Exception("SimpleException");
    LOG.error("ERROR log message with exception", e);
    LOG.info("----------- oddelovac ------------");
    LOG.error("ERROR log message with param \"{}\" and exception", param, e);
  }

}
```
### Logback
Základnou SpringBoot používanou implementáciou SLF4J je [Lockback](http://logback.qos.ch/). Rýchly
a jednoduchý nástroj pre logovanie. Svojou podstatou je pokračovateľom log4j projektu.

Konfigurácia Logback-u sa realizuje za pomoci súboru *logback.xml* kde definujeme výstupy tzv. Appender.
> ${project_home}/src/main/resources.logback.xml            príklad výstupu na konzolu
```xml
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>
                %d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n
            </Pattern>
        </layout>
    </appender>

    <logger name="vd.sandbox.spring" level="debug" additivity="false">
        <appender-ref ref="CONSOLE"/>
    </logger>

    <root level="error">
        <appender-ref ref="CONSOLE"/>
    </root>
</configuration>
```
> ${project_home}/src/main/resources.logback.xml            príklad výstupu do súboru
```xml
<configuration>
    <property name="HOME_LOG" value="logs/app.log"/>
    <appender name="FILE-ROLLING" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${HOME_LOG}</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>logs/archived/app.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
            <!-- each archived file, size max 10MB -->
            <maxFileSize>10MB</maxFileSize>
            <!-- total size of all archive files, if total size > 20GB, it will delete old archived file -->
            <totalSizeCap>20GB</totalSizeCap>
            <!-- 60 days to keep -->
            <maxHistory>60</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d %p %c{1.} [%t] %m%n</pattern>
        </encoder>
    </appender>
    <logger name="vd.sandbox.spring" level="debug" additivity="false">
        <appender-ref ref="FILE-ROLLING"/>
    </logger>
    <root level="error">
        <appender-ref ref="FILE-ROLLING"/>
    </root>
</configuration>
```
> Výstupov môžeme definovať viacero a rovnaký log sa môže (ale nemusi ak nespĺňa podmienky vystupu)
> objaviť na všetkých definovaných výstupoch.
### Log4j2
Vývojáry Log4j sa poučili na svojich chybách a prišli s vylepšeným logovacím frameworkom [Log4j2](https://logging.apache.org/log4j/2.x/)
Tento logovací framework ma rozšírené možnosti logovania a oveľa lepší performance pri naozaj vysokých
záťažiach serverov. Celý logging je výsostne asynchronný avšak vždy dodrží poradie prichádzajúcich logov.

SpringBoot potrebuje však niekoľko úprav knižníc, aby bolo možné používať Log4j2.
```groovy
...
configurations.all {
    exclude group: 'org.slf4j', module: 'slf4j-simple'
}
dependencies {
    implementation 'org.apache.logging.log4j:log4j-slf4j-impl:2.13.0'
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude group: 'org.springframework.boot', module: 'spring-boot-starter-logging'
    }
    compile('org.springframework.boot:spring-boot-starter-log4j2')
    compile('com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.7.3')
...
```
> Pre problémy so zavádzaním viacerých implementácii SLF4J je nutne vynechať *slf4j-simple* zo všetkých
> súčastí aplikácie a treba pridať *spring-boot-starter-log4j2* SpringBoot starter. YAML konfigurák
> je možné prečítať len pri pridaní knižnice pre yaml *jackson-dataformat-yaml*. Ak použijeme xml
> formát túto knižnicu pridávať netreba.

## Scheduler
Spring Scheduler vytvára vlastný ThreadPool pre scheduler a SpringBoot si ho obhospodaruje sám.
Je možnosť prepísať default-né nastavenia, ale ak je možnosť vyhnúť sa tomu, urobte tak.

**@EnableScheduling** anotácia povie SpringBoot serveru, aby si vytvorila vlastný scheduler.

**@Scheduled** anotácia na konfiguráciu schedul-ovaného jobu
> vd.sandbox.spring.SchedulerTasks
```java
@Component
@EnableScheduling
public class SchedulerTasks {

  private static final Logger LOG = LoggerFactory.getLogger(SchedulerTasks.class);

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

  /* Every fixed rate and doesn't check previous one */
  @Scheduled(fixedRate = 5000)
  public void reportCurrentTimeFixRate() {
    LOG.info("FIXED RATE :> The time is now {}", dateFormat.format(new Date()));
  }

  /* Only one execution in one time */
  @Scheduled(fixedDelay = 5000)
  public void reportCurrentTimeFixDelay() {
    LOG.info("FIXED DELAY :> The time is now {}", dateFormat.format(new Date()));
  }

  @Scheduled(fixedDelay = 4000, initialDelay = 1000)
  public void scheduleFixedRateWithInitialDelayTask() {
    LOG.info("FIXED DELAY + INIT DELAY :> The time is now {}", dateFormat.format(new Date()));
  }

  /* CRON approach for scheduling */
  @Scheduled(cron = "0 15 10 15 * ?")
  public void scheduleTaskUsingCronExpression() {
    /* we're scheduling a task to be executed at 10:15 AM on the 15th day of every month. */
    LOG.info("CRON :> The time is now {}", dateFormat.format(new Date()));
  }

  /* Parallel behavior */
  @Async
  @Scheduled(fixedRate = 3000)
  public void scheduleFixedRateTaskAsync() throws InterruptedException {
    LOG.info("ASYNC FIXED RATE :> The time is now {}", dateFormat.format(new Date()));
  }
}
```

## SpringBoot WAR
Vytvoriť deployable SpringBoot WAR je jednoduché, stačí pridať *tomcat-starter* závyslosť a odvodiť "zavádzač"
SpringBoot-u od triedy *org.springframework.boot.web.servlet.support.SpringBootServletInitializer*. 
```groovy
plugins {
    id 'war'
...
dependencies {
    providedRuntime ('org.springframework.boot:spring-boot-starter-tomcat')
...
```
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.

SpringBootServletInitializer;
@SpringBootApplication
public class HelloWorldApplication extends SpringBootServletInitializer {
  public static void main(String[] args) {
    SpringApplication.run(HelloWorldApplication.class, args);
  }
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(HelloWorldApplication.class);
  }
}
```
> Gradle *war* task je preťažený novým taskom **bootWar** ktorý vytvorí **deployeble**, ale aj
> **runnable** WAR file. Teda WAR file ide pustiť rovnako ako JAR.

```shell script
java -jar spring-basic/build/libs/spring-basic-1.0.0-SNAPSHOT.war 
```

> SpringBOOT 2.x : minimálna podporovaná verzia **Tomact 8.5**. [viac](https://dzone.com/articles/spring-boot-20-new-features-infrastructure-changes)

## Literatúra
* [SpringBoot](https://spring.io/guides/gs/spring-boot/)
* [SpringBoot Property Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-application-properties.html)
* [SpringBoot Scheduler](https://github.com/eugenp/tutorials/tree/master/spring-scheduling)
* [Scheduling Tasks](https://spring.io/guides/gs/scheduling-tasks/)
* [Logback Example](https://mkyong.com/logging/logback-xml-example/)
* [Logback Tutorial](https://github.com/eugenp/tutorials/tree/master/logging-modules/logback)
* [Executable and Deployable WAR](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/#packaging-executable-wars)
* [SpringBoot Gradle plugin Tutorial](https://www.baeldung.com/spring-boot-gradle-plugin)
* [SpringBoot Deployment](https://github.com/eugenp/tutorials/tree/master/spring-boot-modules/spring-boot-deployment)
