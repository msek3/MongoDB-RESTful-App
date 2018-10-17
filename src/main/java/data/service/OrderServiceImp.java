package data.service;

import data.orders.Item;
import data.orders.Order;
import data.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.OrderNotFoundException;

import java.util.List;

@Service
public class OrderServiceImp implements OrderService {
    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImp(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order save(Order order){
        return orderRepository.save(order);
    }

    @Override
    public void deleteAll(){
        orderRepository.deleteAll();
    }

    @Override
    public Order findById(String id){
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public void deleteById(String id){
        orderRepository.deleteById(id);
    }

    @Override
    public Order updateOrder(String orderId, Item item){
        return orderRepository.updateOrderById(orderId, item);
    }

    @Override
    public long count(){
        return orderRepository.count();
    }
}
