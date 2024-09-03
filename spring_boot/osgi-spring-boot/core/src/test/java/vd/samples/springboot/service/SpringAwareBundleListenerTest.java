package vd.samples.springboot.service;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.lifecycle.BeforeProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.*;
import vd.samples.springboot.commons.plugins.IPlugin;
import vd.samples.springboot.commons.plugins.dto.ActionResponse;
import vd.samples.springboot.commons.plugins.dto.NotificationResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeast;

@ExtendWith(MockitoExtension.class)
class SpringAwareBundleListenerTest {

    private SpringAwareBundleListener cut;
    private final PluginList pluginList = Mockito.mock(PluginList.class);
    private final BundleEvent bundleEvent = Mockito.mock(BundleEvent.class);
    private final Bundle bundle = Mockito.mock(Bundle.class);
    private final BundleContext bundleContext = Mockito.mock(BundleContext.class);
    private final ServiceReference serviceReference = Mockito.mock(ServiceReference.class);


    @BeforeProperty
    void setUp() {
        cut = new SpringAwareBundleListener(pluginList);
    }

    @Property
    void testActivateBundleEventInvalidPlugin(@ForAll String bundleName) {
        when(bundleEvent.getBundle()).thenReturn(bundle);
        when(bundle.getBundleContext()).thenReturn(bundleContext);
        when(bundle.getState()).thenReturn(Bundle.ACTIVE);
        when(bundle.getSymbolicName()).thenReturn(bundleName);
        cut.bundleChanged(bundleEvent);
        verify(pluginList, never()).register(anyString(), any());
    }

    @Property
    void testActivateBundleEventRegisterService(@ForAll String bundleName) throws InvalidSyntaxException {
        MockPlugin plugin = new MockPlugin();
        when(bundleEvent.getBundle()).thenReturn(bundle);
        when(bundle.getBundleContext()).thenReturn(bundleContext);
        when(bundle.getState()).thenReturn(Bundle.ACTIVE);
        when(bundle.getSymbolicName()).thenReturn(bundleName);
        when(bundleContext.getAllServiceReferences(IPlugin.class.getName(), null)).thenReturn(List.of(serviceReference).toArray(new ServiceReference[0]));
        when(bundleContext.getService(serviceReference)).thenReturn(plugin);
        when(serviceReference.getBundle()).thenReturn(bundle);

        cut.bundleChanged(bundleEvent);

        verify(pluginList).register(bundleName, plugin);
    }

    @Property
    void testUnregisterService(@ForAll String bundleName) throws InvalidSyntaxException {
        MockPlugin plugin = new MockPlugin();
        when(bundleEvent.getBundle()).thenReturn(bundle);
        when(bundle.getBundleContext()).thenReturn(bundleContext);
        when(bundle.getState()).thenReturn(Bundle.ACTIVE);
        when(bundle.getSymbolicName()).thenReturn(bundleName);
        when(bundleContext.getAllServiceReferences(IPlugin.class.getName(), null)).thenReturn(List.of(serviceReference).toArray(new ServiceReference[0]));
        when(bundleContext.getService(serviceReference)).thenReturn(plugin);
        when(serviceReference.getBundle()).thenReturn(bundle);

        cut.bundleChanged(bundleEvent);
        when(bundle.getState()).thenReturn(Bundle.UNINSTALLED);
        cut.bundleChanged(bundleEvent);
        verify(pluginList, atLeast(1)).unregister(bundleName);
    }

    class MockPlugin implements IPlugin {

        @Override
        public ActionResponse doAction() {
            return null;
        }

        @Override
        public NotificationResponse doNotification() {
            return null;
        }
    }


}
