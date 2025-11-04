package zelora.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Wishlist {
    private int wishlistId;
    private Customer customer;
    private Product product;
    private Date addedDate;
    private String wishlistName;
    private String notes;
}
