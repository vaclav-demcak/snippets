---
[_metadata_:title]:- "Kryptovanie hesiel JASYPT"
[_metadata_:author]:- "Václav Demčák"
[_metadata_:date]:- "08/03/2023 9:00 PM"

---

# Kryptovanie hesiel JASYPT

Cieľom tutoriálu je previesť programátora úskaliami pri práci s citlivými dátami (napr. heslami) v programovom
kóde. Rovnako tak, pripraviť základné odporúčania a príklady práce s takýmito dátami tak, aby si ich dotyčný
vedel odskúšať a ohmatať aj sám.

---
*NOTE*

> **Jasypt** (**Ja**va **S**implified Encr**ypt**ion) library

> Pre prezentáciu príkladu riešenia bola vybraná externá knižnica JASYPT. Existujú alternatívy, avšak my sme
> prihliadali primárne na jednoduchosť a rýchlosť integrácie s prostredím SpringBoot-u.
---

## Zásady práce s citlivými dátami

Pravidlá pre nakladanie a prácu s citlivými dátami upravuje vela vyhlášok a zákonov. My sa im venovať rozhodne
nebudeme a pravidlá čo najviac zjednodušíme a formujeme ich do odporúčaní:

* **Kód nesmie obsahovať citlivé dáta** : účty, end-point-y, heslá atď. sa snažíme vytlačiť do **property** súborov, ktoré
    vieme spravovať mimo kód a bez znalosti programovania (manažéri, testeri ...)
* **Citlivé dáta sa snažíme šifrovať** : cieľom je, aby boli dáta nečitateľné tak ľudským okom, ako nástrojmi na prelamovanie
    hesiel. Tj. dodržiavame tzv. externý šifrovací kľúč.
* **Šifrujeme celú informáciu** : napr. pre šifrovanie účtu, šifrujeme tak *Account Name*, ako jeho *Heslo*.
* **Chránime externý šifrovací kľúč** : v prípade dodávky by mal byť externý šifrovací kľúč evidovaný, spravovaný aj dodávaný osobitným spôsobom

## Jasypt rýchle predstavenie a použitie
Pre ukážku rýchleho použitia resp. otestovanie fungovania Jasypt šifrovania nám bude stačiť nasledujúci kód.

```java
/* Create encryptor */
StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
encryptor.setPassword("secret-seed-for-cryptography");
encryptor.setAlgorithm("PBEWithMD5AndTripleDES");

/* Test encryption/decryption */
String privateData = "secret-data";

String encryptedText = encryptor.encrypt(privateData);
assertNotSame(privateData, encryptedText);

String plainText = encryptor.decrypt(encryptedText);
assertEquals(plainText, privateData);
```

Teda z kódu je zrejmé, že šifrovaný reťazec nebude odpovedať pôvodnému, ale vieme ho dešifrovať a potom bude odpovedať pôvodnému.
Teda zašifrované heslo vieme uložiť do databázy a následne ho prečítať a povedať, či prichádzajúce heslo je zhodné s uloženým.
Jednoduchšia metóda overovania zhodnosti reťazcov je nasledujúca:

```java
/* Create encryptor */
String password = "secret-pass";
BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
String encryptedPassword = passwordEncryptor.encryptPassword(password);

/* read User from DB */
UserAcc userAcc = userDaoService.readUserByName(name);

boolean result = passwordEncryptor.checkPassword("secret-pass", userAcc.getPassword());

assertTrue(result);

```

Vidíme tu malý rozdiel použitia Jasypt knižníc na kódovanie / dekódovanie resp. overovanie šifrovaných reťazcov. V zásade hovoríme
o jedno-úrovňovom resp. dvoj-úrovňovom šifrovaní.


### Jedno-úrovňové šifrovanie (One-Way Encryption)
Metodika jedno-úrovňového šifrovania spočíva, že *reťazec* zašifrujeme a uložíme (napr. do databázy). Následne pri overovaní inicializujeme
pôvodnú šifrovaciu triedu a overíme, či sa prichádzajúci reťazec zhoduje s uloženým bez potreby dešifrovať už uložený reťazec. Na prelomenie
takejto šifry nám stačí poznať šifrovaciu triedu resp. šifrovací algoritmus. Teda takto zašifrované dáta, by nemali byť vystavované bez ďalšej
úrovne zabezpečenia.

### Dvoj-úrovňové šifrovanie (Two-Way Encryption)
Metodika dvoj-úrovňového šifrovania spočíva, že *šifrovacia* trieda je zostrojená s vlastným tajným reťazcom slúžiacim k šifrovaniu (tzv. seed)
a na jeho základe realizuje šifrovanie samotných citlivých dát. Tie sa dajú prečítať až po poskytnutí tajného reťazca. Teda dostaneme
dvoj-úrovňové šifrovanie. Odstránime tým potrebu evidencie viacerých hesiel a stále vieme dostatočne silne chrániť citlivé dáta, ktoré sa
poskytujú napr. formou property súborov.

### Sila šifrovania a algoritmy pre šifrovanie
Okrem vlastných tried so silnejším šifrovaním, ktoré nám **jasypt** poskytuje, vieme mu priamo nakonfigurovať aj algoritmus šifrovania
v súlade s [Java Cryptography Extension (JCE)](https://www.oracle.com/java/technologies/javase-jce8-downloads.html). Keďže štúdiom JCE sme
sa nezaoberali do hĺbky, dávame do pozornosti len tieto dva algoritmy (sú integrálnou súčasťou jasypt knižnice):

* **PBEWithMD5AndDES** : je default jasypt šifrovacím algoritmom (napr. pre SpringBoot starter nepotrebuje žiadnu dodatočnú konfiguráciu)
* **PBEWithMD5AndTripleDES** : je silnejšia varianta šifrovania, avšak potrebuje dodatočne konfigurovať (**avšak, odporúčame jej uprednostňovanie**)

## Jasypt SpringBoot integrácia
Pre integráciu potrebujeme upraviť v prvom rade *pom* build súbor a pridať mu závislosť:

```xml
<!-- https://mvnrepository.com/artifact/com.github.ulisesbocchio/jasypt-spring-boot-starter -->
<dependency>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-spring-boot-starter</artifactId>
    <version>3.0.5</version>
</dependency>
```

Následne ešte potrebujeme SpringBoot aplikácii povedať, ktoré časti property súborov sú šifrované.

```properties
encrypted.property=ENC(uTSqb9grs1+vUv3iN8lItC0kl65lMG+8)
```

Teda property obsahujú tzv. marker **ENC( )** definujúci čo sa ma dešifrovať pri inicializačnom načítaní danej property.

---
*WARN*
> Takto nakonfigurovaný SpringBoot automaticky dešifruje reťazce za pomoci **PBEWithMD5AndDES** algoritmu.
> Treba na to preto myslieť pri šifrovaní reťazcov.
---

### PBEWithMD5AndTripleDES Algoritmus
Ak sa rozhodneme používať tento algoritmus, je treba podstrčiť springBoot-u de-šifrovaciu triedu (*Bean*) napr. takto:

```java
@SpringBootApplication
@EnableEncryptableProperties
public class _Runner {
    
  @Value("${spring.jasypt.passphrase}")
  private String passphrase;
    
  public static void main(String[] args) {
    SpringApplication.run(_Runner.class);
  }

  @Bean(name = "jasyptStringEncryptor")
  public StringEncryptor stringEncryptor() {
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setPassword(passphrase); // encryptor's private key
    config.setAlgorithm("PBEWithMD5AndTripleDES");
    config.setKeyObtentionIterations("1000");
    config.setPoolSize("1");
    config.setProviderName("SunJCE");
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setStringOutputType("base64");
    encryptor.setConfig(config);
    return encryptor;
  }
}

```

## Jasypt SpringBoot bez AutoConfiguracie
Resp. ak nepoužívame **@SpringBootApplication** alebo **@EnableAutoConfiguration** môžeme použiť len dependency bez štartéra,
avšak za konfiguráciu zodpovedáme sami.

```xml
<!-- https://mvnrepository.com/artifact/com.github.ulisesbocchio/jasypt-spring-boot -->
<dependency>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-spring-boot</artifactId>
    <version>3.0.5</version>
</dependency>
```

V podstate urobíme všetko to isté, čo by sme robili pri definovaní *PBEWithMD5AndTripleDES Algoritmu*,
avšak aj pre *PBEWithMD5AndDES* algoritmus


## Jasypt Maven Plugin
Posledným veľmi užitočným nástrojom, ktorý *Jasypt Libs* poskytuje je **Jasypt Maven Plugin**. Tento plugin
nám umožňuje rýchle šifrovanie vybraných hodnôt *property* súboru počas build-u resp. za pomoci nástroja maven.

```xml
<!-- https://mvnrepository.com/artifact/com.github.ulisesbocchio/jasypt-maven-plugin -->
<plugin>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-maven-plugin</artifactId>
    <version>3.0.5</version>
</plugin>
```

Teda postup použitia **Jasypt**-u bude nasledovná:

1. Pridanie Jasypt plugin-u do *pom-ka* (viď vyššie)
2. Pridanie anotácie **@EnableEncryptableProperties** (len pre SpringBoot app-ky)
3. Vytvorte si **"tajný kľúč"** (secret key), ktorým budete šifrovať všetky označené hodnoty
4. Zašifrujte / Dešifrujte potrebné *properties*

### **Single String Value**
V prípade, že potrebujeme zašifrovať resp. dešifrovať len špecifický reťazec, môžeme použiť priamo *Jasypt maven plugin*.
Jednoduchým príkazom získame zašifrovanú resp. dešifrovanú podobu definovaného reťazca

#### Šifrovanie
```shell script
mvn jasypt:encrypt-value -Djasypt.encryptor.password=secret_key -Djasypt.plugin.value=Password_for_encrypt
```

* **Input** : 'Password_for_encrypt' (reťazec ktorý potrebuje zašifrovať)
* **Password** (*Secret key*) :  'secret_key' (Tzv. seed je druho-úrovňové heslo, pre použitie šifrovacieho algoritmu)
* **Algoritmus** :  PBEWithMD5AndDES (default algorithm used)

Po spustení uvidíme nasledovné:

```shell script
...
[INFO] Encryptor config not found for property jasypt.encryptor.iv-generator-classname, using default value: org.jasypt.iv.RandomIvGenerator
[INFO] Encryptor config not found for property jasypt.encryptor.string-output-type, using default value: base64
[INFO] 
ENC(16jX1WsOUbuDSuYmad6/VE1vfgLgVjFAu49PeNfLQ3m+PIZmbvsSjk5G8NSP8x0c4uB5wJFGVIGvOs/a5PWgaQ==)
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.749 s
[INFO] Finished at: 2023-03-09T12:30:01+01:00
[INFO] ------------------------------------------------------------------------
```

Teda hodnota *Password_for_encrypt* po zašifrovaní bude *16jX1WsOUbuDSuYmad6/VE1vfgLgVjFAu49PeNfLQ3m+PIZmbvsSjk5G8NSP8x0c4uB5wJFGVIGvOs/a5PWgaQ==*
a môžeme ňou nahradiť skutočnú hodnotu.

---
**INFO**:
> Každé spustenie šifrovacieho mechanizmu nám prinesie novú a odlišnú hodnotu od tej predchádzajúcej, avšak
> spätné dešifrovanie nám vždy vráti pôvodnú zašifrovanú hodnotu.
---

#### Dešifrovanie

Pre overenie vyššie uvedeného tvrdenia môžeme za pomoci maven plugin-u dešifrovať zvolenú hodnotu nasledovne:

```shell script
mvn jasypt:decrypt-value -Djasypt.encryptor.password=secret_key -Djasypt.plugin.value=16jX1WsOUbuDSuYmad6/VE1vfgLgVjFAu49PeNfLQ3m+PIZmbvsSjk5G8NSP8x0c4uB5wJFGVIGvOs/a5PWgaQ==
```

Výsledkom dešifrovania bude mať zhruba nasledovný výstup:

```shell script
...
[INFO] Encryptor config not found for property jasypt.encryptor.iv-generator-classname, using default value: org.jasypt.iv.RandomIvGenerator
[INFO] Encryptor config not found for property jasypt.encryptor.string-output-type, using default value: base64
[INFO] 
Password_for_encrypt
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.727 s
[INFO] Finished at: 2023-03-09T12:36:23+01:00
[INFO] ------------------------------------------------------------------------

```

Teda veľmi užitočný plugin pre rýchle šifrovanie / dešifrovanie hodnôt bez potreby písania šibnutého test kodu.






### **Properties file values**
Keď už vieme za pomoci *maven plugin-u* šifrovať a dešifrovať reťazce, poďme sa pozrieť na použitie pre celý property file.

---
**NOTE**:
> Tento prístup nám umožní šifrovať / dešifrovať hodnoty pri spúšťaní tak, aby sme nemuseli v aplikácii okrem property súboru
> a pom-ka nič meniť. Teda pre udržiavanie potrebných hodnôt napríklad v GitLab-e všetky zašifrujeme a priamo pri vlastnom build-e
> alebo pred spúšťaním *spring-boot:run* resp. iným mvn zavádzačom dešifrovať. Teda vo výsledku sa nám nič nezmení oproti aktuálnemu
> nešifrovanému stavu v GitLab-e.
> **Secret-phrase** : môžeme držať v utajenej sekcii GitLab CI/CD build definitions a tým zabránime k akémukoľvek úniku citlivých dát
---------

Teda zavedieme si do nášho súboru *application-plugin.yml* nasledujúce testovacie hodnoty:

```yaml
test:
  crypt:
    username: Testerson
    password: DEC(JohansonPasswordTesterson)
```

* **DEC**() je marker prefix definujúci, že všetko vo vnútri je treba šifrovať.

```shell script
mvn jasypt:encrypt -Djasypt.encryptor.password=secret_key
```
Keďže máme už navedených viacero profilov, treba nám pridať do mvn príkazu aj spring
profil, aby sa *jasypt* zorientova. Teda pridame premennú *-Dspring.profiles.active=plugin*

```shell script
mvn jasypt:encrypt -Djasypt.encryptor.password="secret_key" -Dspring.profiles.active=plugin
```
Takéto definície budú fungovať iba v prípade *application.properties* resp. *application-plugin.properties*.
My však používame **yaml** definície pre properties parametre. Teda tam nám ešte okrem profilu treba definovať
cestu k súboru na priamo. Teda:

```shell script
mvn jasypt:encrypt -Djasypt.encryptor.password=secret_key -Dspring.profiles.active=plugin -Djasypt.plugin.path="file:src/main/resources/application-plugin.yml"
```

Môžeme si všimnúť, že po zbehnutí skriptu sa **DEC** prefix zmenil na **ENC** prefix. **ENC** prefix už hovorí,  
že hodnota vo vnútri je šifrovaná a je na čitateľovi, aby si ju pred použitím dešifroval.

---
**INFO**:
> Definície prefixov na šifrovanie / dešifrovanie je možné meniť za pomoci property
> *jasypt.encryptor.property.prefix*. Pre viac informácii ohľadom konfigurácii je treba
> navštíviť [link](https://github.com/ulisesbocchio/jasypt-spring-boot/blob/master/README.md)
>
> Rovnako je možné zmeniť aj súbor obsahujúci dáta na šifrovanie / dešifrovanie za
> pomoci plugin-u. Užitočné pre rýchle šifrovanie hodnôt
> ```shell script
> mvn jasypt:encrypt -Djasypt.plugin.path="file:src/main/test/application.properties" -Djasypt.encryptor.password="the password"
> ```
---

---
**WARN**:
My sme v našom príklade **secret_phrase** dali ako súčasť property file-u. Považujeme to za **security-issue** a je treba
pristupovať k používaniu a ukladaniu šifrovacieho kľúča rozumnejšie.
---


## Literatúra a inšpirácia
* [Jasypt orig site](http://www.jasypt.org/)
* [Online Jasypt Encryption/Decryption](https://www.devglan.com/online-tools/jasypt-online-encryption-decryption)
* [Jasypt simple java](https://www.baeldung.com/jasypt)
* [Jasypt SpringBoot](https://www.baeldung.com/spring-boot-jasypt)
* [SpringBoot Jasypt Encrypt](https://medium.com/@javatechie/spring-boot-password-encryption-using-jasypt-e92eed7343ab)
* [Jasypt libs github](https://github.com/ulisesbocchio/jasypt-spring-boot)

Realizovaný príklad je inšpirovaný nasledovnými repozitármi
* [Jasypt sample](https://github.com/eugenp/tutorials/blob/master/jhipster-modules/jhipster-microservice/car-app/pom.xml)
* [Jasypt maven plugin sample](https://github.com/jonas-haeusler/jasypt-maven-plugin)
