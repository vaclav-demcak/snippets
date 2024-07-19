package vd.sandbox.spring.openapi.aaa;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Home redirection to OpenAPI api documentation
 *
 * Generated class from OpenAPI generator with extra postInstalation method
 */
@Controller
public class HomeController {

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    private static YAMLMapper yamlMapper = new YAMLMapper();

    @Autowired
    private BuildProperties buildProperties;

    @Value("classpath:/openapi.yaml")
    private Resource openapi;

    @PostConstruct
    public void postInitialization() {
        LOG.info("initialization done ...");

        LOG.info("    _          _          _       ____                            _");
        LOG.info("   / \\        / \\        / \\     / ___|  ___ _ ____   _____ _ __ | |");
        LOG.info("  / _ \\      / _ \\      / _ \\    \\___ \\ / _ \\ '__\\ \\ / / _ \\ '__|| |");
        LOG.info(" / ___ \\    / ___ \\    / ___ \\    ___) |  __/ |   \\ V /  __/ |   |_|");
        LOG.info("/_/   \\_\\  /_/   \\_\\  /_/   \\_\\  |____/ \\___|_|    \\_/ \\___|_|   (_)");

        LOG.warn("--------- A A A   Services  S T A R T I N G ---------");
        LOG.warn("--------- {} ---------", buildProperties.getName());
        LOG.warn("--------- {} ---------", buildProperties.getVersion());
        LOG.warn("--------- {} ---------", buildProperties.getTime().toString());
        LOG.warn("--------- {} ---------", buildProperties.get("git.hash"));
        LOG.warn("--------- {} ---------", buildProperties.get("builded.by"));
        LOG.info("------------------------------------------------------------------");
    }

    @Bean
    public String openapiContent() throws IOException {
        try(InputStream is = openapi.getInputStream()) {
            return StreamUtils.copyToString(is, Charset.defaultCharset());
        }
    }

    @GetMapping(value = "/openapi.yaml", produces = "application/vnd.oai.openapi")
    @ResponseBody
    public String openapiYaml() throws IOException {
        return openapiContent();
    }

    @GetMapping(value = "/openapi.json", produces = "application/json")
    @ResponseBody
    public Object openapiJson() throws IOException {
        return yamlMapper.readValue(openapiContent(), Object.class);
    }

//    @RequestMapping("/")
//    public String index() {
//        return "redirect:swagger-ui/index.html?url=../openapi.json";
//    }

    @RequestMapping("/")
    public String index() { return "redirect:swagger-ui.html"; }

}
