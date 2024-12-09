package vd.samples.springboot.statemachine.state;

/**
 * @apiNote : order status
 */
public enum OrderStatusEnum {
    WAIT_PAYMENT,
    WAIT_DELIVER,
    WAIT_RECEIVE,
    FINISH;
}
