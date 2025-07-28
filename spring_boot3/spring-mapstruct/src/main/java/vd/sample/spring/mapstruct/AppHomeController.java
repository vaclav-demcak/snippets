package vd.sample.spring.mapstruct;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Home redirection to OpenAPI api documentation
 */
@Controller
public class AppHomeController {

    private static final Logger LOG = LoggerFactory.getLogger(AppHomeController.class);

    private final String JVM_Name =  System.getProperty("java.runtime.name").toString();
    private final String JVM_Version = System.getProperty("java.runtime.version").toString();
    private final String JVM_Vendor = System.getProperty("java.vendor").toString();

    @Autowired
    private Environment environment;

    @Autowired
    private BuildProperties buildProperties;

    @Value("${git.commit.message.short}")
    private String commitMessage;

    @Value("${git.branch}")
    private String branch;

    @Value("${git.commit.id}")
    private String commitId;

    @PostConstruct
    public void postInitialization() throws UnknownHostException {
        LOG.info("... initialization done ...");
        LOG.info("\n \n \n \n"
                + "   ░██████                       ░██                          ░██████                                                            ░██                                 \n"
                + "  ░██   ░██                                                  ░██   ░██                                                           ░██                                 \n"
                + " ░██         ░████████  ░██░████ ░██░████████   ░████████          ░██           ░███████   ░██████   ░█████████████  ░████████  ░██  ░███████   ░███████            \n"
                + "  ░████████  ░██    ░██ ░███     ░██░██    ░██ ░██    ░██      ░█████           ░██              ░██  ░██   ░██   ░██ ░██    ░██ ░██ ░██    ░██ ░██                  \n"
                + "         ░██ ░██    ░██ ░██      ░██░██    ░██ ░██    ░██          ░██           ░███████   ░███████  ░██   ░██   ░██ ░██    ░██ ░██ ░█████████  ░███████            \n"
                + "  ░██   ░██  ░███   ░██ ░██      ░██░██    ░██ ░██   ░███    ░██   ░██                 ░██ ░██   ░██  ░██   ░██   ░██ ░███   ░██ ░██ ░██               ░██           \n"
                + "   ░██████   ░██░█████  ░██      ░██░██    ░██  ░█████░██     ░██████            ░███████   ░█████░██ ░██   ░██   ░██ ░██░█████  ░██  ░███████   ░███████            \n"
                + "             ░██                                      ░██                                                             ░██                                            \n"
                + "             ░██                                ░███████                                                              ░██                                            \n"
                + "                                                                                                                                                                     \n"
                + "---------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"
                + "                         Spring Boot SAMPLE  ... S E R V E R   S T A R T I N G  ...                                                                                  \n"
                + "                                                                                                                                                                     \n"
                + "                   " + buildProperties.getTime().toString() + "                                                                                                      \n"
                + "                   JVM >> " + JVM_Name + " " + JVM_Version + " by " + JVM_Vendor + "                                                                                 \n"
                + "                   " + InetAddress.getLocalHost() + "                                                                                                                \n"
                + "                   " +  InetAddress.getLocalHost().getHostName() + "                                                                                                 \n"
                + "                                                                                                                                                                     \n"
                + "                   " + buildProperties.getName() + "                                                                                                                 \n"
                + "                   " + buildProperties.getVersion() + "                                                                                                              \n"
                + "                                                                                                                                                                     \n"
                + "                   Profiles: >> " + Arrays.toString(environment.getActiveProfiles()) + "                                                                             \n"
                + "                                                                                                                                                                     \n"
                + "                   " + "GIT >$ ("+branch+ ") - " + commitId + "                                                                                                      \n"
                + "                   " + "Commit msg : " + commitMessage + "                                                                                                           \n"
                + "---------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"
        );
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public RedirectView index() {
        return new RedirectView("/swagger-ui/index.html", true);
    }

}
