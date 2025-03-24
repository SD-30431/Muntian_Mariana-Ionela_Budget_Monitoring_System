package com.example.demo.Controllers;

import com.example.demo.BusinessLogic.AdminService;
import com.example.demo.Model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/create")
    public Admin createAdmin(@RequestBody Admin admin) {
        // In real scenario, hash password, etc.
        return adminService.save(admin);
    }

    @GetMapping("/{username}")
    public Admin getAdmin(@PathVariable String username) {
        return adminService.findByUsername(username);
    }
}
