package zelora.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Review {
    private int reviewId;
    private Customer customer;
    private Product product;
    private int rating;
    private String reviewText;
    private Date reviewDate;
    private boolean flaggedAsSpam;
}
