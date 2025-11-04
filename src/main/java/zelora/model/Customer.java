package zelora.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {
    private int customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;
    private Date dateOfBirth;
    private String paymentInfo;
    private String sizePreferences;
    private String vipStatus;
    private String communicationPreferences;
    private Date dateJoined;
    private String city;
}

