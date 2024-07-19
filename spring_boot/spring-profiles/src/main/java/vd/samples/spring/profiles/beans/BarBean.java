package vd.samples.spring.profiles.beans;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class BarBean {

    private static final Logger LOG = LoggerFactory.getLogger(BarBean.class);

    @Value("${test.path.subpath1}")
    private String subpathValue1;

    @Value("${test.path.subpath2}")
    private String subpathValue2;

    @Value("${test.reference.key:defaultValue}")
    private String referencedValue;

    @PostConstruct
    public void postConstruct(){
        LOG.info("loaded BarBean!");
        LOG.info(">>> Subpaht 1 value is >>> " + subpathValue1);
        LOG.info(">>> Subpaht 2 value is >>> " + subpathValue2);
        LOG.info(">>> Referenced value is >>> " + referencedValue);
    }
}
