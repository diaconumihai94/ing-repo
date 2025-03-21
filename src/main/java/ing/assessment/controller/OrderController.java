package ing.assessment.controller;

import ing.assessment.db.order.Order;
import ing.assessment.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Optional<Order> findOrderById(@PathVariable(name="id") Integer id) {
        return orderService.findOrderById(id);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return new ResponseEntity<>(orderService.saveOrder(order), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable(name="id") Integer id, @RequestBody Order orderToUpdate) {
        return orderService.updateOrder(id, orderToUpdate);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable(name="id") Integer id) {
        orderService.deleteOrder(id);
    }
}
