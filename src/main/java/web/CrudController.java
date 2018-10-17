package web;

import data.orders.Item;
import data.orders.Order;
import data.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/rest")
public class CrudController {
    private OrderService orderService;

    @Autowired
    public CrudController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public @ResponseBody List<Order> orders(){
        return orderService.findAll();
    }

    @GetMapping("/orders/{orderId}")
    public @ResponseBody Order getOrderById(@PathVariable String orderId){
        return orderService.findById(orderId);
    }

    @PostMapping(value = "/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Order> addOrder(@RequestBody Order order, UriComponentsBuilder ucb){
        Order saved = orderService.save(order);
        HttpHeaders headers = new HttpHeaders();
        URI locationToURI = ucb.path("/orders")
                .path(saved.getId())
                .build()
                .toUri();
        headers.setLocation(locationToURI);
        return new ResponseEntity<>(saved, headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/orders/{orderId}")
    public @ResponseBody List<Order> deleteOrder(@PathVariable String orderId){
        orderService.deleteById(orderId);
        return orders();
    }

    @PatchMapping("orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Order> updateOrder(@PathVariable String orderId,
                                             @RequestBody Item item,
                                             UriComponentsBuilder ucb){

        Order updated = orderService.updateOrder(orderId, item);
        HttpHeaders headers = new HttpHeaders();
        URI locationToURI = ucb.path("/orders")
                .path(updated.getId())
                .build()
                .toUri();
        headers.setLocation(locationToURI);
        return new ResponseEntity<>(updated, headers, HttpStatus.OK);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody Error orderNotFound(OrderNotFoundException e){
        return new Error(HttpStatus.NOT_FOUND.value(), "order [" + e.getOrderId() + "] not found");
    }
}
