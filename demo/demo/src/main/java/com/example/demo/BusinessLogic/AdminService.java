package com.example.demo.BusinessLogic;

import com.example.demo.Model.Admin;
import com.example.demo.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    // Additional logic for Admin
}
