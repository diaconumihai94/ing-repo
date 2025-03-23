package ing.assessment.service.impl;

import ing.assessment.db.order.Order;
import ing.assessment.db.order.OrderProduct;
import ing.assessment.db.product.Product;
import ing.assessment.db.product.ProductCK;
import ing.assessment.db.repository.OrderRepository;
import ing.assessment.db.repository.ProductRepository;
import ing.assessment.exceptions.InvalidOrderException;
import ing.assessment.exceptions.OutOfStockException;
import ing.assessment.model.Location;
import ing.assessment.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductService productService;

    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderServiceImpl(orderRepository, productRepository, productService);
    }

    @Test
    void saveOrder_WhenProductIsOutOfStock_ShouldThrowOutOfStockException() {
        Order order = new Order();
        order.setOrderCost(200.0);
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProductId(1);
        order.setOrderProducts(Collections.singletonList(orderProduct));

        Product product = new Product();
        product.setProductCk(new ProductCK(1, Location.FRANKFURT));
        product.setQuantity(0);

        when(productRepository.findByProductCk_Id(1)).thenReturn(Collections.singletonList(product));

        OutOfStockException exception = assertThrows(OutOfStockException.class, () -> {
            orderService.saveOrder(order);
        });
        assertEquals("Quantity cannot be 0 for product ID: 1", exception.getMessage());
    }

    @Test
    void saveOrder_WhenOrderHasNoProducts_ShouldThrowInvalidOrderException() {
        Order order = new Order();
        order.setOrderCost(200.0);
        order.setOrderProducts(Collections.emptyList());

        InvalidOrderException exception = assertThrows(InvalidOrderException.class, () -> {
            orderService.saveOrder(order);
        });
        assertEquals("Order must contain at least 1 product", exception.getMessage());
    }

    @Test
    void updateOrder_WhenOrderExists_ShouldUpdateOrder() {
        Order existingOrder = new Order();
        existingOrder.setId(1);
        existingOrder.setOrderCost(100.0);

        Order orderToUpdate = new Order();
        orderToUpdate.setOrderCost(150.0);

        when(orderRepository.findById(1)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        Order updatedOrder = orderService.updateOrder(1, orderToUpdate);

        assertEquals(150.0, updatedOrder.getOrderCost());
        verify(orderRepository, times(1)).save(existingOrder);
    }

    @Test
    void updateOrder_WhenOrderDoesNotExist_ShouldThrowNoSuchElementException() {
        Order orderToUpdate = new Order();
        orderToUpdate.setOrderCost(150.0);

        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            orderService.updateOrder(1, orderToUpdate);
        });
        assertEquals("Order with given id 1 not found", exception.getMessage());
    }

    @Test
    void deleteOrder_ShouldCallDeleteOnRepository() {
        int orderId = 1;

        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }
}
