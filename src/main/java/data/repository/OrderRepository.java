package data.repository;

import data.orders.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String>, CustomOrderRepository {
    List<Order> findAll();
    Order findById();
}
