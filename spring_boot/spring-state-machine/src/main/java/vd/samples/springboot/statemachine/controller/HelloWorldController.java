package vd.samples.springboot.statemachine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import vd.samples.springboot.statemachine.service.OrderService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orig")
public class HelloWorldController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/hello"
    )
    public Map<String, Object> showHelloWorld(){
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "HelloWorld");
        return map;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/testOrderStatusChange"
    )
    public String testOrderStatusChange(){
        orderService.create();
        orderService.create();
        orderService.pay(1L);
        orderService.deliver(1L);
        orderService.receive(1L);
        orderService.pay(2L);
        orderService.deliver(2L);
        orderService.receive(2L);
        System.out.println("all orders：" + orderService.getOrders());
        return "success";
    }

}
