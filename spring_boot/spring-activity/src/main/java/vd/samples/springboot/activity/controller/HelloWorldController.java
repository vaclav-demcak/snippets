package vd.samples.springboot.activity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vd.samples.springboot.activity.service.ActivityConsumerService;

@RestController
public class HelloWorldController {
    @Autowired
    ActivityConsumerService activityConsumerService;

    @RequestMapping(value="/startActivityDemo",method= RequestMethod.GET)
    public boolean startActivityDemo(){
        return activityConsumerService.startActivityDemo();
    }
}
