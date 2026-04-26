package com.bank.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Transaction {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private BigDecimal amount;
    private LocalDateTime timestamp;

    // getters & setters
}