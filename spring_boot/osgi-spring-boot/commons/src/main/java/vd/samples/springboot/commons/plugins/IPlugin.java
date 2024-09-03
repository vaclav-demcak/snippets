package vd.samples.springboot.commons.plugins;

import vd.samples.springboot.commons.plugins.dto.ActionResponse;
import vd.samples.springboot.commons.plugins.dto.NotificationResponse;

public interface IPlugin {

    ActionResponse doAction();

    NotificationResponse doNotification();
}
