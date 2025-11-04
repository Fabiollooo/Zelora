package zelora.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {
    private int productId;
    private String productName;
    private String description;
    private Category category;
    private double price;
    private String size;
    private String colour;
    private String material;
    private Integer sustainabilityRating;
    private String manufacturer;
    private Date releaseDate;
    private double discountedPrice;
    private String featureImage;
    private Supplier supplier;
}
