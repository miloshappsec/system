package model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class User {

    private Long id;
    private String username;
    private String password;
    private String email;
    private BigDecimal balance;

    // getters & setters
}