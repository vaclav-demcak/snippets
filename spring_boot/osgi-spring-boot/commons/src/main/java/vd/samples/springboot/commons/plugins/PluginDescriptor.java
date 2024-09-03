package vd.samples.springboot.commons.plugins;

import lombok.Builder;
import lombok.Getter;

import java.util.Hashtable;

@Getter
@Builder
public class PluginDescriptor {

    Object implementation;
    String name;
    Hashtable<String, Object> params;

}
