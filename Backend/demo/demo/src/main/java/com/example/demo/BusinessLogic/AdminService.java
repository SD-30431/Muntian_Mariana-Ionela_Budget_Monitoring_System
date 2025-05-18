package com.example.demo.BusinessLogic;

import com.example.demo.DTO.AdminLoginRequest;
import com.example.demo.Model.Admin;
import com.example.demo.Repository.AdminRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    public ResponseEntity<?> login(AdminLoginRequest loginRequest) {
        if (validateAdminLogin(loginRequest.getUsername(), loginRequest.getPassword())) {
            Admin admin = findByUsername(loginRequest.getUsername());
            if (admin == null) {
                // fallback if not in DB
                admin = new Admin("admin", "admin");
            }
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("admin", admin);
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    private boolean validateAdminLogin(String username, String password) {
        return "admin".equals(username) && "admin".equals(password);
    }
}
