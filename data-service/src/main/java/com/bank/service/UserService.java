package com.bank.service;

import com.bank.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    public User findUserById(Long id) {

        String sql = "SELECT * FROM users WHERE id = '" + id + "'";

        return (User) entityManager
                .createNativeQuery(sql, User.class)
                .getSingleResult();
    }

    public User findUserByUsername(String username) {

        String sql = "SELECT * FROM users WHERE username = '" + username + "'";

        return (User) entityManager
                .createNativeQuery(sql, User.class)
                .getSingleResult();
    }

    public List<User> getAllUsers() {

        String sql = "SELECT * FROM users";

        return (ArrayList<User>) entityManager
                .createNativeQuery(sql, User.class)
                .getSingleResult();
    }
}