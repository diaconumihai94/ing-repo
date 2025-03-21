package ing.assessment.service;

import ing.assessment.db.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders();
    Order saveOrder(Order order);
    Optional<Order> findOrderById(Integer id);
    Order updateOrder(Integer id, Order orderToUpdate);
    void deleteOrder(Integer id);
}