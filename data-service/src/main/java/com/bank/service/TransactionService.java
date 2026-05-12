package com.bank.service;

import com.bank.model.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private com.bank.repository.TransactionRepository transactionRepository;

    /**
     * Transfers money between two users.
     * <p>
     * VULNERABILITIES:
     * - No ownership check: any caller can transfer from any account (IDOR)
     * - Negative amount allowed: transfer -500 from Bob to Alice increases Bob's balance
     * - Self-transfer allowed: sender == receiver
     * - No atomicity: balance update and transaction log are separate — race condition possible
     * - No balance floor check: balance can go negative
     * - Raw SQL with string concatenation: SQL injection on fromId/toId
     */
    @Transactional
    public Transaction transfer(Long fromId, Long toId, BigDecimal amount) {
        // Raw SQL — no parameterized queries, no balance checks, no ownership verification
        String debit = "UPDATE users SET balance = balance - " + amount + " WHERE id = " + fromId;
        String credit = "UPDATE users SET balance = balance + " + amount + " WHERE id = " + toId;

        entityManager.createNativeQuery(debit).executeUpdate();
        entityManager.createNativeQuery(credit).executeUpdate();

        Transaction tx = new Transaction();
        tx.setSenderId(fromId);
        tx.setReceiverId(toId);
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(tx);
    }

    /**
     * Returns all transactions for a given user ID.
     * <p>
     * VULNERABILITY: No authentication or authorization — any user ID can be queried (IDOR).
     */
    public List<Transaction> getByUserId(Long userId) {
        String sql = "SELECT * FROM transactions WHERE sender_id = " + userId +
                " OR receiver_id = " + userId;

        @SuppressWarnings("unchecked")
        List<Transaction> result = entityManager
                .createNativeQuery(sql, Transaction.class)
                .getResultList();
        return result;
    }
}
