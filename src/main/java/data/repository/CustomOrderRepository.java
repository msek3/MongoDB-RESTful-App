package data.repository;

import data.orders.Item;
import data.orders.Order;

public interface CustomOrderRepository {
    Order updateOrderById(String orderId, Item item );
}
