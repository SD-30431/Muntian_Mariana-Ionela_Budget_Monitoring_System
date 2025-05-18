package com.example.demo.Controllers;

import com.example.demo.DTO.AdminLoginRequest;
import com.example.demo.Model.Admin;
import com.example.demo.BusinessLogic.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @PostMapping("/create")
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
        Admin savedAdmin = adminService.save(admin);
        return ResponseEntity.ok(savedAdmin);
    }
    @GetMapping("/{username}")
    public ResponseEntity<Admin> getAdmin(@PathVariable String username) {
        Admin admin = adminService.findByUsername(username);
        if (admin == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(admin);
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody AdminLoginRequest loginRequest) {
        return adminService.login(loginRequest);
    }
}
