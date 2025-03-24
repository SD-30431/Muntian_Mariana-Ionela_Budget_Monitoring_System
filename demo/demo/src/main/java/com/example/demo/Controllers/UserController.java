package com.example.demo.Controllers;

import com.example.demo.BusinessLogic.UserService;
import com.example.demo.Config.SecurityHelper;
import com.example.demo.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityHelper securityHelper;

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        // Hash the incoming password
        String hashed = securityHelper.hashPassword(user.getPasswordHash());
        user.setPasswordHash(hashed);
        return userService.save(user);
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.findByUsername(username);
    }
}
