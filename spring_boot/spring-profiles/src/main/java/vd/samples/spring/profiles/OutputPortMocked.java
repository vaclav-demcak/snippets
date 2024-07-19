package vd.samples.spring.profiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutputPortMocked implements OutputPort {

    private static final Logger LOG = LoggerFactory.getLogger(OutputPortMocked.class);

    public OutputPortMocked() {
        LOG.info("OutputPort MOCK ...");
    }
}
