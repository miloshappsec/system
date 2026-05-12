package com.bank.controller;

import com.bank.model.User;
import com.bank.repository.UserRepository;
import com.bank.service.KafkaProducerService;
import com.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @GetMapping("/id/{id}")
    public User getById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/username/{username}")
    public User getByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }


    // Internal endpoint — called by api-gateway AdminController
    // No auth here; access control is (weakly) enforced at gateway level
    @GetMapping("/all-internal")
    public List<User> getAllInternal() {
        return userService.getAllUsers();
    }

    @PostMapping("/all")
    public List<User> get(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        User user = userService.findUserByUsername(username);
        if (user != null && user.getPassword().equals(password) && user.getRole().equals("admin")) {
            return userService.getAllUsers();
        }
        return null;
    }

    @PostMapping("create")
    public User create(@RequestBody User user) {
        userService.createUser(user);
        kafkaProducerService.sendUserCreatedEvent(user.getUsername());
        return user;
    }

    /**
     * Update any user account by ID.
     * <p>
     * VULNERABILITIES:
     * - IDOR: no ownership check — any caller can update any user's account
     * - Mass assignment: caller controls all fields including role and balance
     * e.g. send { "role": "admin", "balance": 9999999 } to escalate privileges instantly
     * - No authentication token required
     */
    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User updated) {
        updated.setId(id);
        return userRepository.save(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}