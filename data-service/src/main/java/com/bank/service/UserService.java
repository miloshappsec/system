package com.bank.service;

import com.bank.model.User;
import com.bank.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    public User findUserById(Long id) {

        String sql = "SELECT * FROM users WHERE id = '" + id + "'";

        @SuppressWarnings("unchecked")
        List<User> result = entityManager
                .createNativeQuery(sql, User.class)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public User findUserByUsername(String username) {

        String sql = "SELECT * FROM users WHERE username = '" + username + "'";

        @SuppressWarnings("unchecked")
        List<User> result = entityManager
                .createNativeQuery(sql, User.class)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public List<User> getAllUsers() {

        String sql = "SELECT * FROM users";

        @SuppressWarnings("unchecked")
        List<User> result = entityManager
                .createNativeQuery(sql, User.class)
                .getResultList();
        return result;
    }

    public void createUser(User user) {
        userRepository.save(user);
    }
}