package vd.samples.springboot.commons.plugins;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.BundleContext;
import vd.samples.springboot.commons.plugins.dto.ActionResponse;
import vd.samples.springboot.commons.plugins.dto.NotificationResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AbstractPluginActivatorTest {

    private AbstractPluginActivator cut;
    @Mock
    private BundleContext bundleContext;


    @BeforeEach
    void setUp() {
        cut = new TestActivator();
    }

    @Test
    @DisplayName("When start method invoked, then the plugin service is registered into the bundle context")
    void testStart() throws Exception {
        cut.start(bundleContext);
        verify(bundleContext, times(1)).registerService(eq("vd.samples.springboot.commons.plugins.IPlugin"), any(SampleService.class), any());
    }

    public class TestActivator extends AbstractPluginActivator {

        @Override
        protected PluginDescriptor registerService() {
            return PluginDescriptor.builder().implementation(new SampleService()).name(IPlugin.class.getName()).build();
        }
    }

    public class SampleService implements IPlugin {

        @Override
        public ActionResponse doAction() {
            return ActionResponse.builder().body("sample action").build();
        }

        @Override
        public NotificationResponse doNotification() {
            return NotificationResponse.builder().body("sample notification").build();
        }
    }
}

