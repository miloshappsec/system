package model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Employee {

    private Long id;
    private String name;
    private String role;
    private BigDecimal salary;

    // getters & setters
}