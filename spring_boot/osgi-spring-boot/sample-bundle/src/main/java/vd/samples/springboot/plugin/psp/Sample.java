package vd.samples.springboot.plugin.psp;

import vd.samples.springboot.commons.plugins.IPlugin;
import vd.samples.springboot.commons.plugins.dto.ActionResponse;
import vd.samples.springboot.commons.plugins.dto.NotificationResponse;

public class Sample implements IPlugin {
    @Override
    public ActionResponse doAction() {
        return ActionResponse.builder().body("Action Worked !!").build();
    }

    @Override
    public NotificationResponse doNotification() {
        return NotificationResponse.builder().body("Notification Processed...").build();
    }

}
