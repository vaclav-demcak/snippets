---
[_metadata_:author]:- "Václav Demčák"
[_metadata_:title]:- "Spring JPA Persistance"
[_metadata_:date]:- "25/05/2020 9:00 PM" 

---

# Spring JPA Presistance

Cieľom tutoriálu je previesť programatora zakladnými uskaliami pri tvorbe Spring perzistentnej vrstvy.
Obsahom projektu je len nutný základ, ktorý ma ďalej slúžiť pri trenovaní a prototypovaní zložitejších
štruktúr tak, aby vývojár ušetril čas na zakladnú skladbu projektu a mohol sa priamo venovať len
návrhu riešenia jeho problému.

----
{:toc}
----

## Nutné vedomostné predpoklady
Projekt obsahuje časti iných technológii ktore sú obsahom iných tutoriálov v rôznej urovni zložitosti.
Pre úplné pochopenie obsahu projektu odporúčame prejsť aj nasledovne tutorialy:
* SpringBoot (todo: add link) - konfigurácia a vystavnie Swagger UI servera SpringBoot
* OpenAPI (todo: add link) - vytvorenie Swagger REST rozhrania na testovanie Perzistentnej vrstvy
* Docker Compose (todo: add link) - priprava lokalneho prostredia pre plynulý vývoj (MySQL DB)

## Prvé spustenie
Skôr ako pristúpime k predstaveniu jednotlivých blokov projektu, maly by sme si projekt spustiť
a overiť jeho fuknčnosť. Predpokladom k úspešnému spusteniu projektu je inštalovaný:
* [Docker](https://docs.docker.com/get-docker/) pre inicializáciu okolia (MySQL DB)
* [Gradle](https://gradle.org/install/) pre vlastné spustenie projektu

### Inicializácia databázy
Databaza je nakonfigurovaná za pomoci súboru *docker-compose.yml* a za pomoci inštalovaného dockeru bude automaticky inicializovana za pomoci nasledovného príkazu
```shell script
docker-compose up
```
### Spustenie projektu
Samotný projekt je pripravený ako SpringBoot, takže ho spúšťame ako akýkoľvek SpringBoot za pomoci build-ovacích nástrojov ([viac info tu](https://spring.io/guides/gs/spring-boot/))
##### Gradle
``` shell script
./gradlew bootRun
```
> **Note:** Maven ako build-ovací nástroj nieje zahrnutý v projekte, kedže generovanie OpenAPI sposobuje problémy pri práci s maven-om.
  {: .note} 

Po úsešnom naštartovaní servera, otvoríme browser a možeme sa šantiť na Swagger GUI. [http://localhost:8080/](http://localhost:8080/)
(TODO: add link to generated DOC from OpenAPI endpoints)

## Konfigurácia SpringBoot Perzistentnej vrstvy
Pre prácu s databázou musíme nevyhnutne urobiť nasledovné kroky
#### Konfigurácia závyslosti
Potrebujeme pridať závyslostí do Build script-u pre Spring_JPA a DB_Client_Lib: 
> *build.gradle*
``` shell script
dependencies {
    ...
    compile('org.springframework.boot:spring-boot-starter-data-jpa')
    runtimeOnly 'mysql:mysql-connector-java'
    ...
}
```
#### Konfigurácia pripojenia
Potrebujeme povedať Spring-u, kde je databáza a ako sa na ňu pripojiť.
> *${project_dir}/src/main/resources/application.yml*
``` yaml
...
spring:
  ...
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: jpa-user
    password: jpa-user-password
    url: jdbc:mysql://localhost:3306/spring-jpa-test?createDatabaseIfNotExist=true
...
```
Tymto sme nakonfigurovali **DataSource** spring bean. Povedali sme mu aky driver ma použiť, ake sú *credentials* a url.
> *createDatabeseIfNotExist* slúži na vytvorenie, **POZOR** nevhodné pre produkčné prostredie.
#### Konfigurácia JPA
Spring JPA je [SPI](https://en.wikipedia.org/wiki/Service_provider_interface) pre perzistencie
v spring prostredi. Defaultne je to hibernate. Avšak tieto skutočnosti je treba definovať tiež.
> *${project_dir}/src/main/resources/application.yml*
``` yaml
...
spring:
  ...
  jpa:
    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect
    hibernate:
      ddl-auto: update
      show-sql: true
...
```
> Samozrejme, *application.yaml* je možné konfigurovať aj ako properties file. [diskusia](https://meetsnehal.wordpress.com/2015/09/12/yaml-an-alternative-to-properties-file-with-spring-boot/) [priklad](https://www.baeldung.com/spring-yaml)

V tejto chvíli máme všetky predpoklady pre prácu s databazou hotové a môžeme pristúpiť k vlastnej implementácii kódu.

## Entity
[Entity Bean](https://en.wikipedia.org/wiki/Entity_Bean) je *terminus technikus* používaný pre označnenie [POJO](https://en.wikipedia.org/wiki/Plain_old_Java_object)
objektov reprezentujúcich obsah databázových záznamov jednotlivých tabuliek. Je možné ich do určitej miery prispôsobovať, avšak to nieje
obsahom tohto tutoriálu. My si svoju testovaciu entitu urobime ručne a hibernate nam ju pri inicializacii v databaze aj vytvorí.
> Databázové Entity je možné z databázy aj vygenerovať. Pozri DB Entity generátor tutorial. (todo: add link)
> > **POZOR** : Entity reflection na Databázu je rozhodne neželaný prístup pre produkčné prostredie, preto
> veľmi obozretne konfigurujte property [DDL](https://docs.jboss.org/hibernate/core/3.3/reference/en/html/session-configuration.html#configuration-misc-properties)
> Možné hodnoty: **validate | update | create | create-drop**  Pre produkčné prostredie striktne odporučame **validate**. 

Teda trieda *vd.sandbox.spring.jpa.db.model.User.java* je obrazom databázovej tabuľky **User**.
> *vd.sandbox.spring.jpa.db.model.User*
``` java
@Entity
@Table(name = "User")
public class User {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "userFirstName", length = 100, nullable = false)
  private String userFirstName;

  ...
```
Po príprave všetkých entít môžeme pristúpiť k vytvoreniu repozitárov.

## Repozitáre DAO (**D**ata **A**ccess **O**bject)
[DAO](https://www.tutorialspoint.com/design_pattern/data_access_object_pattern.htm) repozitár je hlavným
kľúčom k používaniu dát z databázy. Základom každého repozitára je CRUD operacie.
[CRUD](https://cs.wikipedia.org/wiki/CRUD) je akronim zo skratiek **C**reate, **R**ead, **U**pdate a **D**elete.
Spring ponúka niekoľko možností predpripravených repozitárov [viac tu](https://www.baeldung.com/spring-data-repositories)
však, my sa budeme zaoberať len CRUD repozitárom a definiciami vlastných metod za pomoci [JPQL](https://www.tutorialspoint.com/jpa/jpa_jpql.htm)
#### Definicia Spring Repozitára
> *vd.sandbox.spring.jpa.db.repo.UserRepository*
``` java
@Repository
public interface UserRepository extends CrudRepository<User, Long> {}
```
> *[org.springframework.data.repository.CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)*
> obsahuje takmer všetky základné operácie nad Databázovou tabuľkou, ktoré by mohol programátor potrebovať
> a nieje potrebné sa trápiť s ich fyzickou implementáciou.

**@Repository** označí *Interface* ako repozitár a Spring si ho už pri kompilácii nalinkuje ako Repozitár.
Pri spustení mu je teda pridaný príslušný *TransactionManager* a metódy na pozadí budú šrotiť SQL dotazy
predpripravené odvodeným *CrudRepository* rozhraním. Definovaný pár *<User, Long>* špecifikuje Entitu
a dátový typ jeho primárneho kľúča.

#### Definicia špecifických požiadaviek na databázu
Občas nam len CRUD nebude stačiť, takže treba doplniť vlastný *select* na databazu. Je niekoľko spôsobov
implementácie, avšak mi preferujeme pridanie žiadanej metódy do **Repozitára**.
> *vd.sandbox.spring.jpa.db.repo.UserRepository*
``` java
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  @Query("FROM User WHERE userFirstName LIKE CONCAT('%',:firstName,'%')")
  List<User> findByFistName(@Param("firstName") String firstName);
}
```
> Mať všetky metódy nad špecifikovanou tabuľkov v jednej triede má nasledovné výhody:
> 1. prehľadnosť - v jednej triede mame všetky podporovanú funkcionalitu
> 2. agilita - zmeny databazovych štruktúr môžeme prenášať za pomoci generátora
> 3. jednoduchosť - SQL je proste databazovy jazyk, tak prečo vymýšľať koleso znova

**@Query** definuje dotaz (je to v podstate alias pre SELECT) a do tela vložíme priamo SQL kód.
**@Param** odporúčame používať kôli jednoznačnosti pri použití viacerých vstupných premenných
a uľahčuje čítanie kódu ostatnými vývojármi. Parametrizácia podľa poradia vstupných parametrov
je neprehľadná a z pohľadu dlhodobejšej údržby *nevhodná*.

## Servisná vrstva a DTO (**D**ata **T**ransfer **O**bject)
Servisná vrstva je vrstva, kde sa implementuje **[business logika](https://en.wikipedia.org/wiki/Business_logic)**.
Tj. v tejto triede dochádza k transformácii a manipulácii s datami. V našom príklade je zastúpená
jednoduchou triedou:
> *vd.sandbox.spring.jpa.api.UserServices*
``` java
@Component
public class UserServices {

  @Autowired
  private UserRepository userRepo;

  public Optional<UserResponseDto> add(UserDto dto) {
    // tu je mozne urobit validacie, pripadne dalsie nutne ukony
    User user = userRepo.save(UserMapper.INSTANCE.mapToEntity(dto));
    return user != null ? Optional.of(UserMapper.INSTANCE.mapToResponseDto(user)):Optional.empty();
  }
  ...
```
> **Odporúčanie**: logika a datove transformácie je lepšie robiť nad DTO objektami. Entity by mali
> slúžiť primárne len a len na priamu komunikáciu s Databazou -> e.g. CRUD.

**@Component** Servisa by mala byť prístupná vrámci Spring kontajneru komukoľvek a jeho inštanciu si
spring bude strážiť sám.  

**@Autowired** DTO repozitárov je opäť kontrolovaný Spring kontajnerom a programátor sa už nemusí
trápiť s transakciami ani inými úskaliami perzistentnej vrstvy. Kombinácia viacerých repozitárov
v rámci jednej servisnej triedy nieje ničím výnimočným a netreba sa toho báť.

Vlasntá servisná logika je už vecou definovaných [workflow](https://en.wikipedia.org/wiki/Workflow)
a skutočných požiadaviek zákazníka a funkcionality. Tu je dôležité len dbať na vstupne a výstupné
objekty. **Vstup aj výstup každej servisnej metódy by mali byť len a len
jednoduché dátové typy alebo DTO objekty.**

Serializacia z Entit na DTO objekty sú v príklade realizované za pomoci [mapstruct](https://mapstruct.org/).
> *vd.sandbox.spring.jpa.api.UserMapper*
``` java
@Mapper
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User mapToEntity(UserDto dto);
  UserDto mapToDto(User entity);
  UserResponseDto mapToResponseDto(User entity);
}
```
Keďže mená parametrov sú totožné, nieje treba definovať žiadne extra definicie pre *mapstruct* mapovadlo.
Pre hlbšie pochopenie práce s *mapstruct*, je vytvorený iný tutorial. (todo: link na mapstruct projekt)

## Komunikačná vrstva - Web Service 
[Web Services](https://en.wikipedia.org/wiki/Web_service) je definované komunikačné API. V rámci
jedného projektu môžeme podporovať viacero komunikačných protokolov s rovnakou logikou. Z tohto
dôvodu sa snažíme izolovať logiku v servisnej vrstve a propagovať do komunikačnej vrstvy len a len
výsledky metód. Napr. zmena zo SOAP na REST je teda veľmi jednoduchá.
 
Keďže v našom príklade nám API *komunikačnej vrstvy* generuje OpenAPI a my implementujeme len
premostenie na servisnú vrstvu. (todo: linka na - viac o OpenAPI) 
> *vd.sandbox.spring.jpa.api.UsersApiController*
``` java
@Controller
@RequestMapping("${openapi.springJPASampleUserAPIDocumentation.base-path:}")
public class UsersApiController implements UsersApi {

    @Autowired
    private UserServices services;

    ...

    public ResponseEntity<List<UserResponseDto>> v1UsersGet() {
        try {
            return ResponseEntity.ok(services.getUsers());
        } catch (Exception ex) {
            return new ResponseEntity(ex.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    ...
```
> **Web Service Controller** by mal len transformovať výstup zo servisnej vrstvy a obaľovať známe
> i neznáme výnimky do zrozumiteľnejšej formy v súlade so špecifikáciou [HTTP status Code](https://en.wikipedia.org/wiki/List_of_HTTP_status_codes). 

## Spring konfigurácia
V zásade platí, že spring spúšťame za pomoci triedy označenej **@SpringBootApplication** anotáciou.
Tu by sme mali definovať aj skenery jednotlivých komponent. (*vd.sandbox.spring.jpa.invoker.OpenAPI2SpringBoot*)
**@ComponentScan** anotácia slúži na definiciou balíkov obsahujúcich spring komponenty.
> Nám však SpringBoot zavádzajúcu triedu generuje OpenAPI a necítime potrebu ju písať manuálne. Teda
> pre pridanie definicií skenerov SpringBoot zavádzača, použijeme Spring konfiguračný bean.

> *vd.sandbox.spring.jpa.api.ApplicationConfig*
``` java
@Configuration
@EntityScan(basePackages = {"vd.sandbox.spring.jpa.db.model"})
@EnableJpaRepositories(basePackages = {"vd.sandbox.spring.jpa.db.repo"})
@EnableTransactionManagement
public class ApplicationConfig { }
```
**@Configuration** - definuje Spring konfiguračný bean tj. je načítaný spoločne s komponentami 
(teda pozor, musí byť umiestnený v balíku pre skener komponenty) a môžeme tu pridať aj dodatočné
konfiguraky, ktoré niesu súčasťou suboru *application.yml*.

**@EntityScan** - definuje list balíkov s entitami pre Entity skener a môže ukazovať aj na ine 
importované projekty ktoré obsahuju Entity Beany (POJO triedy označene anotáciou **@Entity**)

**@EnableJpaRepositories** - definuje list balikov s JPA DTO repozitármi pre Repository skener.
Repozitáre musia byť označené anotáciou **@Repository**

**@EnableTransactionManager** - definicia zapntutia podpory manažmentu transakcii. Správny prístup
pre používania anotácie @Transactional je už mimo tohto tutorialu. V základe len spomenieme, že
Spring Transactional podporuje:
* Propagation Type
* Isolation Level
* Timeout
* readOnly
* Rollback rules
> Doporučujeme používať @Transactional anotácie priamo na metódy DAO Repozitárov, ak to konštrukcia
> perzistentnej vrstvy aspoň trochu dovoľuje a treba ju používať s rozvahou.

## Literatúra a inšpirácia
* [Spring Data](https://spring.io/projects/spring-data)
* [Accessing Data JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Spring JPA Doc by ver.](https://docs.spring.io/spring-data/jpa/docs/)
* [Spring Repository](https://www.baeldung.com/spring-data-repositories)
* [JPQL](https://www.tutorialspoint.com/jpa/jpa_jpql.htm)
* [Declarative Transaction](https://www.logicbig.com/tutorials/spring-framework/spring-data-access-with-jdbc/correct-use-of-declarative-transaction.html)

Realizovaný príklad je inšpirovaný nasledovnými repozitármi
* [Sample DOG](https://github.com/amitrp/dog-service-jpa)
* [Sample Baeldung](https://github.com/eugenp/tutorials/tree/master/persistence-modules/spring-persistence-simple)