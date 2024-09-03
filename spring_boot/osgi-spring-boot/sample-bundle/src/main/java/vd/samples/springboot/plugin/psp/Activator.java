package vd.samples.springboot.plugin.psp;

import lombok.extern.slf4j.Slf4j;
import vd.samples.springboot.commons.plugins.AbstractPluginActivator;
import vd.samples.springboot.commons.plugins.IPlugin;
import vd.samples.springboot.commons.plugins.PluginDescriptor;

import java.util.Hashtable;

@Slf4j
public class Activator extends AbstractPluginActivator {

    @Override
    protected PluginDescriptor registerService() {
        Hashtable<String, Object> props = new Hashtable<>();
        props.put("Plugin-Name", "SamplePlugin");
        return PluginDescriptor.builder()
                .implementation(new Sample())
                .name(IPlugin.class.getName())
                .params(props)
                .build();
    }
}
