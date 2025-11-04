package zelora.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Inventory {
    private int inventoryId;
    private Product product;
    private int quantityInStock;
    private int quantityReserved;
    private int reorderPoint;
    private Supplier supplier;
}
