package com.bank.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.protocol.types.Field;

import java.math.BigDecimal;

@Getter
@Setter
public class User {

    private Long id;
    private String username;
    private String password;
    private String email;
    private BigDecimal balance;
    private String bankNumber;

    // getters & setters
}