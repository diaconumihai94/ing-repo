package ing.assessment.db.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Date timestamp;

    @ElementCollection
    private List<OrderProduct> orderProducts;

    @NotNull
    @PositiveOrZero(message = "Order cost must be zero or greater")
    private Double orderCost;

    @NotNull
    @PositiveOrZero(message = "Delivery cost must be zero or greater")
    private Integer deliveryCost = 30;

    @NotNull
    @PositiveOrZero(message = "Delivery time must be zero or greater")
    private Integer deliveryTime = 2;
}