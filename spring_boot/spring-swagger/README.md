
[_metadata_:author]:- "Václav Demčák"
[_metadata_:title]:- "Spring Swagger"
[_metadata_:date]:- "03/06/2020 14:32 PM" 


# Spring Swagger

Cieľom tutorálu je previesť programátora základnou konštrukciou pre [Swagger](https://en.wikipedia.org/wiki/Swagger_(software)).
**Swagger** je veľmi šikovný nástroj vývojara pre design, build, dokumentáciu a konzumovanie RESTfull [web servisov](https://en.wikipedia.org/wiki/Web_API).
Keďže WebServices je základom pre prezentačnú vrstvu a vo finálnej miere definujú komunikačné kontracty Web [API](https://en.wikipedia.org/wiki/Application_programming_interface)
ich riadna prezentácia, dokumentácia a distribúcia sú kľúčové časti každej aplikácie.

## Obsah
1. [Prvotné spustenie](#Prvotné-spustenie)
2. [Konfigurácia závyslostí](#Konfigurácia-závyslostí)
    1. [Plugins](#Plugins)
    2. [Depenencies](#Depenencies)
3. [Konfigurácia SpringBoot Runner-u](#Konfigurácia-SpringBoot-Runner-u)
    1. [Build dependencies](#Build-dependencies)
    2. [Configuration Bean](#Configuration-Bean)
    3. [Schema Endpoint](#Schema-Endpoint)
4. [Spring REST API](#Spring REST API)
    1. [Inteface](#Inteface)
    2. [Implementácia](#Implementácia)
5. [Home Redirection](#Home-Redirection)
6. [Spring Package Scanner](#Spring-Package-Scanner)

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
Týmto sme povedali SpringBoot web kontajneru, že bude počúvať na porte *8080*.

## Konfigurácia Swagger-u
Swagger zahŕňa celú škálu úkonov na pozadí bez vedomia programátora. Väčšinu zdrojov dostane
priamo od SpringBoot kontajnera, ale je nutné správne nakonfigurovať niekoľko častí.

### Build dependencies
Pre správnu konfiguráciu, musíme najprv pridať Swagger knižnice do build-u.
> ${project_dir}/build.gradle
``` groovy
dependencies {
    ...
    compile("io.springfox:springfox-swagger2:2.9.2")
    compile("io.springfox:springfox-swagger-ui:2.9.2")
    ...
}
```

### Configuration Bean
Následne musíme správne nakonfigurovať Swagger **Docket** bean.
> vd.sandbox.spring.swagger.SwaggerConfiguration
``` java
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

  ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Spring Swagger Simple Sample API Documentation")
        .description("Simple Stupid Message from SpringBoot REST endpoint.")
        .license("")
        .licenseUrl("http://unlicense.org")
        .termsOfServiceUrl("")
        .version("1.0.0")
        .contact(new Contact("Vaclav Demcak","", "vaclav.demcak@gmail.com"))
        .build();
  }

  @Bean
  public Docket customImplementation(ServletContext servletContext, @Value("${base-path:}") String basePath) {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("vd.sandbox.spring.swagger.api"))
        .build()
        .pathProvider(new BasePathAwareRelativePathProvider(servletContext, basePath))
        .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
        .directModelSubstitute(java.time.OffsetDateTime.class, java.util.Date.class)
        .apiInfo(apiInfo());
  }

  class BasePathAwareRelativePathProvider extends RelativePathProvider {
    ...
  }
}
```
Anotácia *@Configuration* deklaruje spring-u, že sa jedná o configuračný bean.

Anotácia *@EnableSwagger2* deklaruje, že má spring použiť knižnice Swagger2 (Swagger je stale
vo vývoji a my by sme radi používali ten najnovší)

Metóda **ApiInfo apiInfo()** plni Objekt *[ApiInfo](http://springfox.github.io/springfox/javadoc/2.6.0/index.html?springfox/documentation/service/ApiInfo.html)*
základnými informaciami, ktore sa zobrazujú v záhlavy načítanej stránke Swagger-u.

Metóda **Docket customImplementation(ServletContext servletContext, @Value("${base-path:}") String basePath)**
nám definuje *Swagger Docket* tj. Swagger Dokumentačný scanner a kontroler. Definujeme mu, ktorý
package obsahuje Swagger dokumentaciu na parsovanie a v build time Swagger zariadi generovanie
všetkého nevyhnutného kodu na správnu funkcionalitu celeho Swagger API zobrazenia.

Trieda **class BasePathAwareRelativePathProvider extends RelativePathProvider** pomáha definovať
relatívne cesty na dokumentované API.

### Schema Endpoint
Posledným želaným bodom konfigurácie je vystavnie schémy pre možnosť generovať klienta z URL resp.
pre validácie prípadných zmien zo strany klienta. **POZOR: hovoríme o Swagger scheme/definicii!**
> ${project_dir}/src/main/resources/application.yml
``` yaml
...
springfox:
  documentation:
    swagger:
      v2:
        path: /api-docs
```
Touto konfiguráciou sme povedali sprinfox-u, kde ma dať swagger schému vyparsovaných web servisov.

## Spring REST API
V tomto bode môžeme smelo pristúpiť k implementácii REST API web servisov. Je dobrým zvykom rozdeľovať
implementáciu webových servisov na Interface a vlastnú implementáciu.

### Inteface
Do inerface-u deklarujeme jednotlivé metódy API kontraktov. Keďže deklarácia je priestorova nenáročná,
pridávame tam všetku dokumentáciu a cesty pre endpointy. Teda interface obsahuje deklaráciu a popisy.
> vd.sandbox.spring.swagger.api.MessageApi
``` java
@Api(value = "hello", description = "Endpoint for Hello specific operations")
public interface MessageApi {

  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  @ApiOperation(value = "Returns param", nickname = "printMessage", notes = "Returns param", response = String.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successful retrieval of param value", response = String.class) })
  @RequestMapping(value = "/{msg}",
      produces = { "application/json" },
      method = RequestMethod.GET)
  default ResponseEntity<String> printMessage(@ApiParam(value = "",required=true) @PathVariable("msg") String msg) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
```
> Java8 nám dovoľuje pridať do deklarácie metódy aj telo. Teda *return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);*
je veľmi dobrý marker pre testerov, že daná metóda ešte nieje implementovaná a pritom dovoľuje mať vlastnú
implementáciu v každom čase funkčnú, čistú a deploy-ovateľnú.

> Java class name postifx **Api** je tiež dobrým zvykom označovania API deklarácii kontraktov. 

### Implementácia
Triedy s vlastnou implementáciou API zvačša obsahujú len validácie vstupov a prebaľovanie výstupov
Servisnej vrstvy do objektov deklarovaného Web Servisného rozhrania (napr. REST, SOAP atď.).
Avšak pre náš príklad obsahujú trapne jednoduchú logiku typu "Hello Word!"
> vd.sandbox.spring.swagger.api.MessageController
``` java
@Controller
@RequestMapping("/hello/${base-path:}")
public class MessageController implements MessageApi {

  private final NativeWebRequest request;

  @Autowired
  public MessageController(NativeWebRequest request) {
    this.request = request;
  }

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.ofNullable(request);
  }

  public ResponseEntity printMessage(String msg) {
    String result = "Hello " + msg + "!";
    return ResponseEntity.ok(result);
  }
}
```
> Java class name postifx **Controller** je tiež dobrým zvykom označovania implementácie API kontraktov.

Anotácia **@Controller** hovorí spring kontajneru, že sa jedná o kontroler *MessageApi* deklarácie.

Anotácia **@RequestMapping("/hello/${base-path:}")** definuje endpoint tejto implementácie. Totiž,
jedna deklarácia umožňuje mať viacero implementácii (napr. v rôznych verziach).

## Home Redirection
Poslednou súčasťou projektu je kontrola API redirection. Keďže http://localhost:8080 neobsahuje žiadny
web, prajeme si byť automaticky odprevadený na Swagger webovú štruktúru na adrese http://localhost:8080/swagger-ui.html#/
Túto funkcionalitu nam zabezpečuje nasledujúca komponenta:
> vd.sandbox.spring.swagger.HomeController
``` java
@Controller
public class HomeController {

  @RequestMapping("/")
  public String index() {
    return "redirect:swagger-ui.html";
  }

}
```

## Spring Package Scanner
Už máme všetko hotové a pripravene. Avšak Spring kontajner ešte nevie, kde má hľadať svoje komponenty.
Teda potrebujeme SpringBoot zavádzaču povedať, ktoré balíky ma prejst, aby si našiel to čo potrebuje.
> vd.sandbox.spring.swagger.SampleSpringBootRunner
``` java
@SpringBootApplication
@ComponentScan(basePackages = {"vd.sandbox.spring.swagger", "vd.sandbox.spring.swagger.api"})
public class SampleSpringBootRunner {

  public static void main(String[] args) throws Exception {
    new SpringApplication(SampleSpringBootRunner.class).run(args);
  }
}
```

Anotácia **@ComponentScan** obsahuje list balikov, ktore ma pri spúšťaní spring kontajner prejsť na
vyhľadanie výskytu tried označených ako *komponenta* resp. *konfigurácia*.

## Literatúra
* [Springfox Reference](https://springfox.github.io/springfox/docs/snapshot/)
* [SpringBoot tutorials](https://www.baeldung.com/spring-boot)
* [SpringBoot Gradle Build](https://spring.io/guides/gs/gradle/)
* [Spring RES tutorials](https://www.baeldung.com/rest-with-spring-series)
* [Spring REST API with Swagger2](https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api)

