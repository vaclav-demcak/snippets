package vd.samples.springboot.oracle;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AppHomeController {

    private static final Logger LOG = LoggerFactory.getLogger(AppHomeController.class);

    @Autowired
    private Environment environment;

    @Autowired
    private BuildProperties buildProperties;


    @PostConstruct
    public void init() {
        LOG.info("... initialization done ...");
        LOG.info("\n \n \n \n"
                + "  ██████╗     ██████╗      █████╗      ██████╗    ██╗         ███████╗            ███████╗ █████╗ ███╗   ███╗██████╗ ██╗     ███████╗    \n"
                + " ██╔═══██╗    ██╔══██╗    ██╔══██╗    ██╔════╝    ██║         ██╔════╝            ██╔════╝██╔══██╗████╗ ████║██╔══██╗██║     ██╔════╝    \n"
                + " ██║   ██║    ██████╔╝    ███████║    ██║         ██║         █████╗              ███████╗███████║██╔████╔██║██████╔╝██║     █████╗      \n"
                + " ██║   ██║    ██╔══██╗    ██╔══██║    ██║         ██║         ██╔══╝              ╚════██║██╔══██║██║╚██╔╝██║██╔═══╝ ██║     ██╔══╝      \n"
                + " ╚██████╔╝    ██║  ██║    ██║  ██║    ╚██████╗    ███████╗    ███████╗            ███████║██║  ██║██║ ╚═╝ ██║██║     ███████╗███████╗    \n"
                + "  ╚═════╝     ╚═╝  ╚═╝    ╚═╝  ╚═╝     ╚═════╝    ╚══════╝    ╚══════╝            ╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝╚═╝     ╚══════╝╚══════╝    \n"
                + "                                                                                                                                         \n"
                + "-----------------------------------------------------------------------------------------------------------------------------------------\n"
                + "                     O R A C L E   SAMPLE   S e r v e r    S T A R T I N G                                                        \n"
                + "                                 " + buildProperties.getName() + "               \n"
                + "                                 " + buildProperties.getVersion() + "               \n"
                + "                                 " + buildProperties.getTime().toString() + "               \n"
                + "                                 Profiles: " + Arrays.toString(environment.getActiveProfiles()) + "               \n"
                + "---------------------------------------------------------------------------------------------------------------------------------\n"
        );
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public RedirectView index() {
        return new RedirectView("/swagger-ui/index.html", true);
    }
}
