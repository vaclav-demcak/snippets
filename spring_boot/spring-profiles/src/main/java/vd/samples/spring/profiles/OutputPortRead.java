package vd.samples.spring.profiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutputPortRead implements OutputPort {

    private static final Logger LOG = LoggerFactory.getLogger(OutputPortRead.class);

    public OutputPortRead() {
        LOG.info("OutputPort READ ...");
    }
}
