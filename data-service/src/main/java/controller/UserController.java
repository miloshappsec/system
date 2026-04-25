package controller;

import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/users/{id}")
    public User get(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/users/{username}")
    public User get(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }


    @PostMapping("/users")
    public List<User> get(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        User user = userService.findUserByUsername(username);
        if (user != null && user.getPassword().equals(password) && user.getRole().equals("admin")) {
            return userService.getAllUsers();
        }
        return null;
    }
}