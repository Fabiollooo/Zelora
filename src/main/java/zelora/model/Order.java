package zelora.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {

    private int orderId;
    private Customer customer;
    private Date orderDate;
    private double totalAmount;
    private String orderStatus;
    private String paymentMethod;
    private String shippingMethod;
    private Date deliveryDate;

}

