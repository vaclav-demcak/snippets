package vd.samples.springboot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.osgi.framework.*;
import org.springframework.stereotype.Component;
import vd.samples.springboot.commons.plugins.IPlugin;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class SpringAwareBundleListener implements BundleListener {

    private final PluginList pluginList;

    @Override
    public void bundleChanged(BundleEvent bundleEvent) {

        log.info(
                String.format(
                        "Bundle %s:%s changed state to %s - Type: %s",
                        bundleEvent.getBundle().getSymbolicName(),
                        bundleEvent.getBundle().getVersion(),
                        getBundleStateAsString(bundleEvent.getBundle().getState()),
                        getBundleStateAsString(bundleEvent.getType())
                )
        );

        if (bundleEvent.getBundle().getState() == Bundle.ACTIVE) {
            try {
                Bundle bundle = bundleEvent.getBundle();
                BundleContext bundleContext = bundle.getBundleContext();
                ServiceReference<?>[] services = bundleContext.getAllServiceReferences(IPlugin.class.getName(), null);
                if (services != null && services.length > 0)
                    Arrays.asList(services)
                            .forEach(s -> pluginList.register(s.getBundle().getSymbolicName(), (IPlugin) bundleContext.getService(s)));

            } catch (InvalidSyntaxException e) {
                log.warn("Problem reading services from BundleContext");
            }

        } else if (bundleEvent.getBundle().getState() == Bundle.UNINSTALLED) {
            pluginList.unregister(bundleEvent.getBundle().getSymbolicName());
        }

    }

    private String getBundleStateAsString(int state) {
        return switch (state) {
            case Bundle.ACTIVE -> "Active";
            case Bundle.INSTALLED -> "Installed";
            case Bundle.RESOLVED -> "Resolved";
            case Bundle.STARTING -> "Starting";
            case Bundle.STOPPING -> "Stopping";
            case Bundle.UNINSTALLED -> "Uninstalled";
            default -> "Unknown";
        };

    }
}
