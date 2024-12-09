package vd.samples.springboot.statemachine.state;

import lombok.Data;

@Data
public class Order {
    private Long orderId;
    private OrderStatusEnum orderStatus;
}
