package vd.samples.springboot.statemachine.service;


import vd.samples.springboot.statemachine.state.Order;

import java.util.Map;

public interface OrderService {

    Order create();

    Order pay(long id);

    Order deliver(long id);

    Order receive(long id);

    Map<Long, Order> getOrders();
}
