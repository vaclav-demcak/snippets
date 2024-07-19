---
title: "SpringBoot Oracle DB Pool sandbox"
author: Václav Demčák
date: March 1rd, 2023
---


# SpringBoot a Oracle database

> Oracle v minulosti poskytoval svoje JDBC driver-e dosť pokútnym spôsobom
> sťahovania s podmienkou registrácie. Našťastie tieto časy su už preč
> a OJDBC sú už dnes súčasťou Centrálneho Maven Repository ([link](https://mvnrepository.com/search?q=com.oracle.database.jdbc))

Ľahká dostupnosť JDBC driver-ov pre Oracle DB server, je len jeden
vyriešený problém. Ďalší je voľba správneho driver-u pre náš projekt.

## Základná tabuľka verzii

---
**NOTE**
> Exaktnú tabuľku sa mi nepodarilo nájsť. Toto som vypísal z popisov
> z rôznych zdrojov a nie všetky sa zhodovali. Tak prosím, neberte to
> ako exaktný manuál.
---

| OJDBC   |                   JDK                      | Oracle DB Server |
|---------|--------------------------------------------|------------------|
| ojdbc8  | JDK8, JDK11, JDK12, JDK13, JDK14 and JDK15 | 12c - 19c
| ojdbc10 | JDK10 and JDK11                            | 19c
| ojdbc11 | JDK11, JDK12, JDK13, JDK14 and JDK15       | 19c - 21c
|  ...    | ...                                        | ...


Do svojho projektu teda pridáme závislosť veľmi ľahko. Napr. pom.xml :
``` xml
    <dependency>
      <groupId>com.oracle.database.jdbc</groupId>
      <artifactId>ojdbc8</artifactId>
      <version>21.5.0.0</version>
    </dependency>
```

Teda na prvý pohľad máme vystarané a už len nakonfigurovať DataConnector.

## Konfigurácia

SpringBoot používa Auto-Configure procedúru, teda ak nám netreba používať
zverstvá typu PLSQL atď., máme zmáknuté veľmi ľahko v súbore:

> *${project_dir}/src/main/resources/application.yml*
resp.
> *${project_dir}/src/main/resources/application.properties*

```yaml
spring:
  ## Spring DATASOURCE (DataSourceAutoConfiguration & DataSourceProperties)
  datasource:
    driver-class-name: oracle.jdbc.OracleDriver
    url: jdbc:oracle:thin:@//dev19db.teran.sk:1521/ais19 # jdbc:oracle:thin:@<IP>:1521/<ServiceName>
    username: ENC(frxMLvHVc3F62uIKfT5jzH0Bw/wT8D5D)
    password: ENC(Nz3IIyUiYJ9ANlvRAyiHuQ==)
```

SpringBoot default-ne používa Database ConnectionPool Hikari. Je ho tiež
možné konfigurovať [HikariCP config prop list](https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby),
alebo vymeniť za Oracle UCP DataSource Pool (`com.oracle.database.jdbc.ucp`).

## TNS URL Format
Až do bodu, kedy JPA alebo Liquibase váš konfigurák skutočne nepotrebuje
na pripojenie, sa môže javiť všetko v poriadku.

[Oracle Dokumentácia](https://docs.oracle.com/en/database/oracle/oracle-database/21/jajdb/)
špecifikuje TNS URL Formát nasledovne:
```shell script
jdbc:oracle:thin:@[[protocol:]//]host1[,host2,host3][:port1][,host4:port2] [/service_name][:server_mode][/instance_name][?connection properties]
```
Príklady:
```shell script

    jdbc:oracle:thin:@mydbhost:1521/mydbservice
    jdbc:oracle:thin:@tcp://mydbhost:1521/mydbservice
    jdbc:oracle:thin:@tcp://mydbhost1,mydbhost2:1521/mydbservice
    jdbc:oracle:thin:@tcp://mydbhost1:5521,mydbhost2:1521/mydbservice
    jdbc:oracle:thin:@tcp://mydbhost1:5521/mydbservice:dedicated
    jdbc:oracle:thin:@mydbhost1:5521/mydbservice?oracle.net.httpsProxyHost=myproxy&oracle.net.httpsProxyPort=80
    jdbc:oracle:thin:@tcps://mydbhost1:5521/mydbservice?wallet_location=/work/wallet
    jdbc:oracle:thin:@tcps://mydbhost1:5521/mydbservice?wallet_location=/work/wallet&ssl_server_cert_dn="Server DN"

```

### TNS:listener problem

Do teraz sa všetko zdalo byť fajn. Žiaľ občas sa môže stať, že uvidíte nasledujúcu hlášku:

---
**NOTE**
> 2023-03-03 00:24:02 - HikariPool-1 - Starting...
> 2023-03-03 00:24:03 - HikariPool-1 - Exception during pool initialization.
> java.sql.SQLRecoverableException: Listener refused the connection with the following error:
> ORA-12514, TNS:listener does not currently know of service requested in connect descriptor
>   (CONNECTION_ID=L/b+aPpES1e3S2i7Xj4OaQ==)
> 	at oracle.jdbc.driver.T4CConnection.handleLogonNetException(T4CConnection.java:893)
---

#### Analýza
Použitá TNS nieje správne interpretovaná a Connector nieje schopný nájsť *End-Point* pre pripojenie.
Stáva sa to, keď pri zriadení Databázy máme len SID a nemáme ServiceName. Tento stav môžeme
otestovať za pomoci [Sqldeveloper-a](https://www.oracle.com/database/sqldeveloper/).
Ten nám umožňuje na pripojenie použiť tak SID, ako ServiceName.

### Riešenie 1#
Všeobecne sa prikláňame k riešeniu, kedy je inštancii databázy pridaný ServiceName. Dodržiavame tak všeobecné
štandardy a do budúcnosti sa vyhneme podobným problémom. Viď nasledujúcu linku [link](https://stackoverflow.com/questions/10786782/ora-12514-tnslistener-does-not-currently-know-of-service-requested-in-connect-d)
Teda overíme výskyt v `tnsnames.ora` požadovaného *SERVICE_NAME* pre našu databázu.

```oracle-sql
select value from v$parameter where name='service_names'
```

Ak tam teda nieje, pridáme registráciou `SERVICE_NAME` aliasu do *tnsnames.ora*.

```oracle-plsql
TEST =
   (DESCRIPTION =
    (ADDRESS_LIST =
      (ADDRESS = (PROTOCOL = TCP)(HOST = *<validhost>*)(PORT = *<validport>*))
    )
    (CONNECT_DATA =
      (SERVER = DEDICATED)
      (SERVICE_NAME = *<servicenamefromDB>*)
    )
)

```

### Riešenie 2#
Ak nemáme aktuálne dosah na databázu a nevieme registrovať pre našu databázu *SERVICE_NAME*,
musíme prinútiť *Hikari*, aby interpretovala TNS URL Format pre SID a nie pre ServiceName.
Tj. URL v property súbore, musí obsahovať TNS definiciu :

---
```TEXT
> jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST =(ADDRESS =(PROTOCOL=TCP)(HOST=[host])(PORT=[port])))(CONNECT_DATA=(SID=[db_sid])(GLOBAL_NAME=[global_sid])(SERVER=[server_mode])))
```
---

Teda náš konfiguračný súbor pre SpringBoot obsahuje nasledujúce nastavenia:

```yaml
spring:
  datasource:
    driver-class-name: oracle.jdbc.OracleDriver
    url: jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST =(ADDRESS =(PROTOCOL=TCP)(HOST=dev19db.teran.sk)(PORT=1521)))(CONNECT_DATA=(SID=ais19)(SERVER=DEDICATED))) # Own TNS
    username: ENC(frxMLvHVc3F62uIKfT5jzH0Bw/wT8D5D)
    password: ENC(Nz3IIyUiYJ9ANlvRAyiHuQ==)
```

## JPA Hibernate Dialect
Ďalšim problémom s ktorým sa pri práci s Oracle Database stretneme, je dialekt.
Žiaľ vyššie verzie Oracle DB majú veľa problémov s dialektami vyššími 12c.

---
**WARN**
> Skutočne doporučujeme používať aj pre 19c aj pre 21c dialekt 12c v JPA nastaveniach
---

```yaml
spring:
  jpa:
    hibertnate:
      ddl-auto: update
      show-sql: true
    open-in-view: false
    database-platform: org.hibernate.dialect.Oracle12cDialect
    properties:
      hibernate:
        temp:
          use_jdbc_metadata_defaults: false
```


## Literatúra a inšpirácia
* [SpringBoot prop](https://gist.github.com/natthakan159/ca092e83fa5391cedeb76087c7d6b198)
* [Hikari](https://www.baeldung.com/hikaricp)
* [OracleConnection Pooling](https://www.baeldung.com/spring-oracle-connection-pooling)
* [Liquibase](https://www.baeldung.com/liquibase-refactor-schema-of-java-app)
* [Oracle Doc](https://docs.oracle.com/en/database/oracle/oracle-database/21/jajdb/)
