package ing.assessment.service.impl;

import ing.assessment.db.order.Order;
import ing.assessment.db.order.OrderProduct;
import ing.assessment.db.product.Product;
import ing.assessment.db.repository.OrderRepository;
import ing.assessment.db.repository.ProductRepository;
import ing.assessment.exceptions.InvalidOrderException;
import ing.assessment.exceptions.OutOfStockException;
import ing.assessment.model.Location;
import ing.assessment.service.OrderService;
import ing.assessment.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order saveOrder(Order order) {
        validateOrder(order);
        computeOrderAndDeliveryCost(order);

        return orderRepository.save(order);
    }

    private void validateOrder(Order order) {
        for (OrderProduct p : order.getOrderProducts()) {
            List<Product> products = productRepository.findByProductCk_Id(p.getProductId());

            boolean isAnyProductOutOfStock = products.stream()
                    .anyMatch(pr -> pr.getQuantity() == 0);

            if (isAnyProductOutOfStock) {
                throw new OutOfStockException("Quantity cannot be 0 for product ID: " + p.getProductId());
            }
        }

        if (order.getOrderProducts().isEmpty()) {
            throw new InvalidOrderException("Order must have at least 1 product");
        }
    }

    private void computeOrderAndDeliveryCost(Order order) {
        if (order.getOrderCost() >= 1000) {
            order.setDeliveryCost(0);
            order.setOrderCost(order.getOrderCost() * 0.9);
        } else if (order.getOrderCost() >= 500) {
            order.setDeliveryCost(0);
        }

        List<Product> productList = new ArrayList<>(productService.getProductsById(order.getOrderProducts().get(0).getProductId()));

        if (productList.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product");
        }

        Set<Location> locations = productList.stream()
                .map(product -> product.getProductCk().getLocation())
                .collect(Collectors.toSet());

        order.setDeliveryTime(locations.size() == 1 ? 2 : locations.size() * 2);

    }

    public Optional<Order> findOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    public Order updateOrder(Integer id, Order orderToUpdate) {
        Optional<Order> order = orderRepository.findById(id);

        return order.map(presentOrder -> {
            presentOrder.setOrderCost(orderToUpdate.getOrderCost());
            presentOrder.setOrderProducts(orderToUpdate.getOrderProducts());
            presentOrder.setTimestamp(orderToUpdate.getTimestamp());
            presentOrder.setDeliveryTime(orderToUpdate.getDeliveryTime());
            presentOrder.setDeliveryCost(orderToUpdate.getDeliveryCost());

            return orderRepository.save(presentOrder);
        }).orElseThrow(() -> new NoSuchElementException(String.format("Order with given id %s not found", id)));
    }

    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }
}