package web;

public class OrderNotFoundException extends RuntimeException {
    private String orderId;

    public OrderNotFoundException(String orderId){
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
