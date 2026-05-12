package com.bank.controller;

import com.bank.model.Transaction;
import com.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * Transfer money between accounts.
     * Body: { "fromId": 1, "toId": 2, "amount": 100.00 }
     * <p>
     * VULNERABILITIES:
     * - No auth token required
     * - No ownership check (IDOR — transfer from any account)
     * - Negative amounts allowed (send -500 to gain money)
     * - No balance check (overdraft freely)
     */
    @PostMapping("/transfer")
    public Transaction transfer(@RequestBody Map<String, Object> body) {
        Long fromId = Long.valueOf(body.get("fromId").toString());
        Long toId = Long.valueOf(body.get("toId").toString());
        BigDecimal amount = new BigDecimal(body.get("amount").toString());

        return transactionService.transfer(fromId, toId, amount);
    }

    /**
     * Get transaction history for any user.
     * VULNERABILITY: No auth — enumerate anyone's transactions (IDOR).
     */
    @GetMapping("/{userId}")
    public List<Transaction> getTransactions(@PathVariable Long userId) {
        return transactionService.getByUserId(userId);
    }
}
