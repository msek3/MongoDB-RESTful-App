package data.repository;

import data.orders.Item;
import data.orders.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import web.OrderNotFoundException;

@Repository
public class CustomOrderRepositoryImpl implements CustomOrderRepository{
    private MongoOperations operations;

    @Autowired
    public CustomOrderRepositoryImpl(MongoOperations operations) {
        this.operations = operations;
    }

    public Order updateOrderById(String orderId, Item item){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(orderId));
        Order order = operations.findOne(query, Order.class);
        if(order == null)
            throw new OrderNotFoundException(orderId);
        order.addItem(item);
        operations.save(order);
        return order;
    }
}

