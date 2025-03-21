package ing.assessment.service.impl;

import ing.assessment.db.order.Order;
import ing.assessment.db.repository.OrderRepository;
import ing.assessment.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> findOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    public Order updateOrder(Integer id, Order orderToUpdate) {
        Optional<Order> tempOrder = orderRepository.findById(id);
        if (tempOrder.isPresent()) {
            Order presentOrder = tempOrder.get();
            presentOrder.setOrderCost(orderToUpdate.getOrderCost());
            presentOrder.setOrderProducts(orderToUpdate.getOrderProducts());
            presentOrder.setTimestamp(orderToUpdate.getTimestamp());
            presentOrder.setDeliveryTime(orderToUpdate.getDeliveryTime());
            presentOrder.setDeliveryCost(orderToUpdate.getDeliveryCost());
            return orderRepository.save(presentOrder);
        }
        return null;
    }

    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }
}