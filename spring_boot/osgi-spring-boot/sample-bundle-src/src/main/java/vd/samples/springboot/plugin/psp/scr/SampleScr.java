package vd.samples.springboot.plugin.psp.scr;

import lombok.extern.slf4j.Slf4j;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import vd.samples.springboot.commons.plugins.IPlugin;
import vd.samples.springboot.commons.plugins.dto.ActionResponse;
import vd.samples.springboot.commons.plugins.dto.NotificationResponse;

@Component(service = IPlugin.class)
@Slf4j
public class SampleScr implements IPlugin {

    @Activate
    public void activate() {
        log.info("Activating Service via SCR");
    }

    @Override
    public ActionResponse doAction() {
        return ActionResponse.builder().body("SCR Action Worked !!").build();
    }

    @Override
    public NotificationResponse doNotification() {
        return NotificationResponse.builder().body("SRC Notification Processed...").build();
    }

}
