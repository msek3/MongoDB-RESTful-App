package data.service;

import data.orders.Item;
import data.orders.Order;

import java.util.List;

public interface OrderService {
    List<Order> findAll();
    Order save(Order order);
    void deleteAll();
    Order findById(String id);
    void deleteById(String id);
    Order updateOrder(String orderId, Item item);
    long count();

}
