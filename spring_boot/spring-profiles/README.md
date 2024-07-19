---
title: "SpringBoot Profile sandbox"
author: Václav Demčák
date: March 21rd, 2023
---

# SpringBoot a Profily

> Profily v SpringBoot aplikáciach predstavuje externé parametrizovanie
> vnútorného behu aplikácie bez potreby **rebuild** procedúry.
> Primárne sa viažu na rôzne typy Runtime okolitého Environment-u (e.g. produkcia, test atď.)
> Ale môžeme ho použiť aj na parametrizovanie rôznych vnútorných nastavení
> napr. rôzne verzie klientov pre servisy tretích strán atď.

## SpringBoot externé nastavenia
Externé nastavenia SpringBoot servera sa vykonávajú za pomoci **resources** súborov :
* **application.properties**
* **application.yml** resp. **application.yaml**

---
**WARN**
> Pre nastavenia je možné použiť len jeden **application** súbor, a to buď vo formáte
> **YAML** alebo vo formáte **properties** . Nieje možné načítať oba súčasne.
> V obsahu zápisu sú oba formáty rovnocenné a líšia sa len formou zápisu cesty **property**
> * properties :
> ``` properties
> spring.profiles.active = foo
> ```
> * YAML :
> ``` yaml
> spring:
>    profiles:
>       active: foo
> ```
> alebo kratšia forma vhodná pre nerozvetvené property
> ``` yaml
> spring.profiles.active: foo
> ```
---

### Vlastný property file
SpringBoot umožňuje aj možnosť definovať vlastný názov property file a aj jeho umiestnenie.  
Avšak vyžaduje to skúsenosti a prax pre správne použitie.

V kóde to môžeme realizovať za pomoci anotácie **@PropertySource** (viď príklad):

```java
@Configuration
@PropertySource("classpath:own-config.properties")
@PropertySource("classpath:own-config-${spring.profiles.active}.properties")
@Component
class OwnConfigurationModule {

  @Value("${own.conf.value.key:defaut_value}")
  private String configValue;
  
  // ... some your code here
}
```

---
**WARN**
NEODPORÚČAME TAKÉTO DEFINÍCIE LEBO CHYBY ČÍTANIA **prop** SÚBOROV SA ŤAŽKO HĽADAJÚ.
Takouto definíciou kdekoľvek v kóde rozbijeme default načítavanie **application.yml**.
Sranda je, že kým sa takto definovaná trieda nenačíta do ClassLoader-a SpringBoot-u,
platia default hodnoty. Žiaľ mi nevieme zabezpečiť poradie load-u tried v SpringBoot
kontajnery. Takže **RUKY PREČ** od takejto definície.

Ak však takáto potreba vyvstane, **SILNO ODPORÚČAME** použiť klasický java properties
loading postup. **Rozhodne nie SpringBoot anotácie !!!**

---

### Properties (nastavenia)
Pre jednotlivé nastavenia hovoríme o **pároch** tj. **kľúč** a **hodnota**. Kľúč je často
označovaný aj, ako **cesta**, a to hlavne v YAML formáte, keďže časť cesty môžu mať dve
hodnoty totožné (viď nasl. príklad):
``` yaml
spring:
  application.name: SpringBoot Profile Sample
  profiles.active: foo
```

Do properties môžeme dať akékoľvek potrebné nastavenia tak vlastné, ako SpringBoot vlastnosti.
* [Referencie SpringBoot Server Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)

## Čítanie hodnôt v kóde
Vlastné hodnoty vieme prečítať za pomoci **anotácie Values** a vieme im aj nastaviť default-nú hodnotu,
ak by načítanie danej property zo súboru zlyhalo. Viď príklad:
```java
@Configuration
public class BaseConfiguration {

    @Value("${spring.application.name:\"Default Application Name\"}")
    private String applicationName;

    // ... some code 
}
```

## Profil a zoskupenie profilov
Profil vytvárame tzv. **postfix-om** v mene súboru **application.yml**. Teda

* **application-bar.yml** resp. **application-bar.properties** : bude mať profil s menom **bar**
* **application-foo.yml** resp. **application-foo.properties** : bude mať profil s menom **foo**
* **application-local.yml** resp. **application-local.properties** : bude mať profil s menom **local**
* **application-prod.yml** resp. **application-prod.properties** : bude mať profil s menom **prod**

Takto definované profily potom zanášame do kódu prostredníctvom anotácie **@Profile**. Viď príklad:
```java
@Configuration
public class BaseConfiguration {

    @Bean
    @Profile("bar")
    BarBean barBean() {
        return new BarBean();
    }

    @Bean
    @Profile("local")
    OutputPort mockedOutputPort(){
        return new OutputPortMocked();
    }

    @Bean
    @Profile("!local") /* pre vsetky profily okrem local */
    OutputPort realOutputPort(){
        return new OutputPortRead();
    }

}
```

### Definovanie aktívneho profilu
Definovať profil sa dá viacerými spôsobmi. Prejdeme si ich postupne.

* **application.yml**
> profil vieme zadefinovať v základnom application súbore za pomoci kľúča
> **spring.profiles.active**. V zásade je to list názvov profilov. Takto
> definovaný aktívny profil je možné zmeniť za pomoci vstupnej hodnoty
> profilu pri spúšťaní. V zásade teda hovoríme o nejakom **default**
> nastavení.
> ``` yaml
> spring.profiles.active: foo,local
> ```

* **SpringBoot start input property**
> Priamo pri štarte servera SpringBoot, môžeme vložiť hodnotu pre profil
> za pomoci prepínača **SPRING PROFILES ACTIVE**
> ```shell script
> java -jar -Dspring.profiles.active=dev,bar XXX.jar
> ```
> Takto definovaný profil prepíše hodnotu v application.yml a nastaví profil
> podľa vstupnej definície.

* **SpringBoot hard-coded profile**
> Ďalšou avšak nie veľmi používanou metódou je "tvrdé" tzv. programové pridanie
> profilu do kódu definujúceho štartovanie Aplikačného servera. Viď príklad pridania
> **bar** profilu. V zásade je to nežiadúci príklad a na jeho korektné použitie je
> treba veľmi špecificky use-case. Avšak takáto definícia **len doplní** profil.
> ```java
> @SpringBootApplication
> public class _Runner {
>
>   public static void main(String[] args) {
>		SpringApplication application = new SpringApplication(_Runner.class);
>		application.setAdditionalProfiles("bar");
>		application.run(args);
>	}
>}
> ```

* **anotáciou @ActiveProfile**
> Poslednou možnosťou je definovania aktívneho profilu za pomoci anotácie **@ActiveProfile**.
> Avšak táto cesta je určená len a len pre **Unit Test**.
> ```java
> @SpringBootTest
> @ActiveProfiles({"foo", "bar"})
> public class FooBarProfileTest {
>
>   @Test
>   void test() {
>   }
> }
> ```


## Literatúra a inšpirácia
* [SpringBoot profiles](https://reflectoring.io/spring-boot-profiles/)
* [SpringBoot profiles Sample](https://github.com/thombergs/code-examples/tree/master/spring-boot/profiles)
* [Baeldung SpringBoot profiles](https://www.baeldung.com/spring-profiles)
* [Baeldung @Values](https://www.baeldung.com/spring-value-defaults)
* [Reference other property](https://www.mscharhag.com/spring/property-references)
