
[_metadata_:author]:- "Václav Demčák"
[_metadata_:title]:- "Spring Swagger"
[_metadata_:date]:- "05/06/2020 14:32 PM" 

# Spring Boot configuration
Cieľom tutoriálu je previesť programátora základnými nastaveniami kontajnera [SpringBoot](https://spring.io/projects/spring-boot)
Prezentačnou pomôckou nám bude Web rozhranie realizované za pomoci [Swagger](https://en.wikipedia.org/wiki/Swagger_(software)).

## Obsah
1. [Prvotné spustenie](#Prvotné-spustenie)
2. [Konfigurácia závyslostí](#Konfigurácia-závyslostí)
    1. [Plugins](#Plugins)
    2. [Depenencies](#Depenencies)
3. [Konfigurácia SpringBoot Runner-u](#Konfigurácia-SpringBoot-Runner-u)
    1. [YAML](#YAML)
    2. [AutoConfig](#AutoConfig)
4. [Property source](#Property-source)
    1. [Vlastný súbor](#Vlastný-súbor)
    2. [Build time hodnoty](#Build-time-hodnoty)
5. [Property reference](#Property-reference)
    1. [@Configuration](#@Configuration)
    2. [@Value](#@Value)
    3. [@PropertySource](#@PropertySource)
    4. [@ConfigurationProperties](#@ConfigurationProperties) 
6. [Profiles](#Profiles)
    1. [@Configuration](#@Configuration)
    2. [Dodatočné informácie](#Dodatočné-informácie)
[](#)


[Literatúra](#Literatúra)

## Prvotné spustenie
Projekt sa v základe skladá len z holého SpringBoot servera a nepotrebuje žiadne dodatočné vedomosti.
Jednotlivými častami konfigurácie a vystavenia Swagger Endpointu prejdeme spolu v tomto tutoráli.
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
    implementation("org.springframework.boot:spring-boot-starter-web:2.1.8.RELEASE")

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
so servletmi (tj. web rozhranim). Swagger ma vlastné webové rozhranie, preto je táto knižnica nutná.
**spring-boot-starter-test** je základom pre test kontajner spring komponent. Ale v tejto chvíli
sa ním zaoberať nebudeme. 
> ${project_dir}/build.gradle
``` groovy
dependencies {
    ...
    compile('com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.7.3')
    ...
}
```
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
### YAML
[YAML](https://sk.wikipedia.org/wiki/YAML) sme vybrali zámerne, aj keď písanie nieje tak pohodlné,
orientácia a vyhľadávanie počas údržby je oveľa prehľadnješia a ľudskému oku a mozgu príjemnejšia.
> Pre potrebu rozsiahlejších konfigurácií, odporúčame rozdeliť ich do viacero súborov. 

> Keďže nami používaný SpringBoot nemá podporu čítania a parsovania YAML property files (okrem application.yml)
> museli sme pridať Util triedu *vd.sandbox.spring.config.api.util.YamlPropertySourceFactory* ktorá
> zapezpečí správny preklad z YAML formatu aj nami definovaných property súborov. 
> [read more](https://itnext.io/why-should-we-prefer-the-yaml-file-over-the-properties-file-for-configurations-in-spring-boot-f31a273a923b)

### AutoConfig
SpringBoot poskytuje možnosť [AutoConfiguration](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/#using-boot-auto-configuration),
čo v praxy znamená, že na základe definovaných properties, pri spustení vytvorí a zavedie všetky
potrebné referenčné triedy z konfiguráku. Je to veľmi pohodlné hlavne keď nepotrebujeme mať úplnú
kontrolu nad inštanciami jednotlivých *funkčných* tried (bean-ov).

## Property source
Už sme si predstavili základy SpringBoot source property file. Ale ten nemusí byť jediným zdrojom
nastavení v našej aplikácii. Môžeme použiť vlastný property file a môžeme využiť aj property ktoré
vzniknú pri build-ovaní našej applikácie teda zdroje sú:
* **application.yml / application.properties**
* **vlastny-file.yml / vlastny-file.properties**
* **build time properties**
### Vlastný súbor
V applikácii si urobíme vlastný property súbor. V našom prípade *moje-property.yml* a do neho si vieme
zadefinovať súbor nastavení ktoré chceme čítať v runtime aplikácie.

> Tu je podstatné zabezpečiť, aby bol súbor riadne pripojený do balika na správne miesto. Pre tento
> účel odporúčame aby všetky proprety súbory boli umiesťňované na jedno miesto v projekte
> **${project_dir}/src/main/resources/** Následne nieje treba špecifikovať cestu, iba meno súboru.
### Build time hodnoty
Prax často prináša potrebu vedieť akú verziu resp. GIT HASH má aktuálna bežiaca inštancia. Pre tieto
a podobné prípady, je možné dotazovať sa na property, ktoré sú skrité resp. ktoré sú definované
v build skripte. Základné informácie o builde je možné získať z triedy
*org.springframework.boot.info.BuildProperties*.

Rozhranie BuildProperties
> org.springframework.boot.info.BuildProperties
```java
String getGroup();
String getArtifact();
String getName();
String getVersion();
Instant getTime();
```
Pridanie dodatočných build informácii.
> ${project_dir}/build.gradle
```groovy
...
def gitCommitHash = 'git rev-parse --verify HEAD'.execute().text.trim()
def buildUser = System.properties['user.name']
...
```
Pre účel demonštrácie *Good Practice* sme urobili Web Servise poskytujúci [informácie o builde](http://localhost:8080/config/app-info).

## Property reference
Súbory s properties je jedna vec, ale treba vediet property aj prečítať a distribuovať.
### @Configuration
Spring anotácia **@Configuration** definuje triedu, ktorá má priami prístup k súboru *application.yml*.
V tele triedy predpokladáme definicie Spring beans metód anotovanými anotáciou **@Bean**.
```java
@Configuration
class ConfigurationFactory {
 
    @Bean
    NeededBean myNeededBean() {
        return new NeededBean();
    }
}
``` 
> Spring Beans referencie je možné vkladať do kódu kdekoľvek v rámci Spring štruktúr **@Component**,
> **@Controller**, **@Service**, **@Repository** atď. za pomoci anotácie **@Autowired**. Viac o @Bean v jeho
> [API](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/annotation/Bean.html)
### @Value
Spring anotácia **@Value** reprezentuje priamu referenciu na property zo súboru *application.yml*.
```java
@Value("${property.key}")
private String propertyByKey;
@Value("${property.key:defaultValue}")
private String propertyByKeyWithDefaultValue;
@Value("#{systemProperties['priority']}")
private String spelValue;
```
> @Value referenciu je možné použiť kdekoľvek v spring konfiguračných komponentoch.
### @PropertySource
Spring anotácia **@PropertySource** nám pomôže prečítať špecifikovaný konfiguračný súbor.
```java
@Configuration
@ConfigurationProperties
@PropertySource(value = "classpath:mojeProperties.yml", factory = YamlPropertySourceFactory.class)
public class ConfigMojeProperties {

    private String meno;
    private String priezvisko;
 
    // standard getter and setters
}
```
### @ConfigurationProperties
Spring anotácia **@ConfigurationProperties** nám pomôže filtrovať špecifické property podľa prefixu.
```java
@Configuration
@ConfigurationProperties("moje-property")
@PropertySource(value = "classpath:mojeProperties.yml", factory = YamlPropertySourceFactory.class)
public class ConfigMojePropertiesFiltered {

    private String name;
    private String haluz;
 
    // standard getter and setters
}
```

## Profiles
Profiles je veľmi užitočný nástroj obmien nastavení pre rôzny druh environment-ov. Príkladom môže byť
používanie Databáz v prostrediach pre vývojarov, testerov, alebo v rôznych produkčných prostrediach.

> Cieľom je teda jeden a ten istý kód, ale rôzne nastavenia pre rôzne prostredia. Avšak občas vyvstane
> požiadavka jemne odlišného správania pre rôzne prostredia. Aj tento problem vieme riešiť pomocou
> Spring Profile prístupom.
### @Configuration
Najrozšírenejšie použitie profilov je práve pre zabezpečenie obmeny konfigurovateľných premenných.
Prístup je jednoduchý, len zadefinujeme správne sources pre používanie profilov.
> vd.sandbox.spring.config.ConfigProfileProperties
```java
@Configuration
@ConfigurationProperties
@PropertySources({
    @PropertySource(value = "classpath:profiledProperties.yml", factory = YamlPropertySourceFactory.class),
    @PropertySource(value = "classpath:profiledProperties-${spring.profiles.active}.yml", factory = YamlPropertySourceFactory.class),
})
public class ConfigProfileProperties implements IProfileProperties {

  private String staticProperty;
  private String dynamicEnvironment;
  private String dynamicMessage;

  public void setStaticProperty(String staticProperty) { this.staticProperty = staticProperty; }

  @Override
  public String getDynamicEnvironment() { return dynamicEnvironment; }

  public void setDynamicEnvironment(String dynamicEnvironment) { this.dynamicEnvironment = dynamicEnvironment; }

  @Override
  public String getDynamicMessage() { return dynamicMessage; }

  public void setDynamicMessage(String dynamicMessage) { this.dynamicMessage = dynamicMessage; }

  @Override
  public String getStaticProperty() { return staticProperty; }
}
``` 
> V jednom čase by mal byť aktívny len jeden profil. Na zabezpečenie tejto skutočnosti, odporúčame
> deklarovať a referencovať dané properties za pomoci interface rozhrania. Tj. SpringBoot bude
> v start-time kričať, že je aktívných viacero inštancii jedného interface-u.

Teda vytvoríme si interface, ktorý budeme propagovať skrz kód a jeho aktívnu **Spring Bean**
implementáciou pre každý profil.
> vd.sandbox.spring.config.api.IProfileProperties  Jednoduchý Interface na distribúciu hodnôt.
```java
public interface IProfileProperties {
    String getStaticProperty();
    String getDynamicEnvironment();
    String getDynamicMessage();
}
``` 
### Dodatočné informácie
Použítím **@Profile** anotáciami pre **@Components**, **@Bean**, atd. sa zaoberať nebudeme. Čitateľ
určite nájde vhodné tutorialy a prípadne rozšíry tento. Avšak každý developer by mal mať na pamäti,
že akékoľvek použite **@Profile** mimo Properties, zo sebou nesie veľké riziko straty kontroly nad
kódom a sťažuje testovanie aplikácie.

Už len maličkosť, ako spustiť SpringBoot so správnym profilom:
```shell script
java -Dspring.profiles.active=prod -jar application.jar
``` 
```shell script
./gradlew <task> -Pprod
``` 
Pre run v IDE je treba pridať environment variable *SPRING_PROFILES_ACTIVE=prod*.

> Občas sa stane, že aplikáciu sa pokúšajú spúšťať aj neznalí ľudia. Preto vrelo odporúčame vyplniť
> default active profil spring property v application.yml konfiguráku.
```yaml
server:
  port: 8080
spring:
  profiles:
    active: dev
...
```   

## Literatúra
* [SpringBoot Property Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-application-properties.html)
* [SpringBoot Scheduler](https://github.com/eugenp/tutorials/tree/master/spring-scheduling)
* [Spring Bean](https://www.baeldung.com/spring-bean)
* [Spring Profiles](https://github.com/eugenp/tutorials/tree/master/spring-core-2)
* [SpringBoot AutoConfig](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/#using-boot-auto-configuration)
* [Properties with Spring](https://github.com/eugenp/tutorials/tree/master/spring-boot-modules/spring-boot-properties)
* [Gradle tutorial](https://www.vogella.com/tutorials/GradleTutorial/article.html)
* [Gradle vs Maven](https://dzone.com/articles/gradle-vs-maven)
* [Build manifest](http://andresalmiray.com/customize-jar-manifest-entries-with-maven-gradle/)
* [Build version](https://www.vojtechruzicka.com/spring-boot-version/)

