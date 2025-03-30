package com.example.demo.BusinessLogic;

import com.example.demo.Model.Admin;
import com.example.demo.Repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminServiceTests {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByUsername() {
        String username = "testUser";
        Admin admin = new Admin();
        admin.setUsername(username);

        when(adminRepository.findByUsername(username)).thenReturn(admin);

        Admin result = adminService.findByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(adminRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testSave() {
        // Given
        Admin admin = new Admin();
        admin.setUsername("testUser");

        // When saving, simulate the repository returning the same admin instance
        when(adminRepository.save(admin)).thenReturn(admin);

        // Execute
        Admin savedAdmin = adminService.save(admin);

        // Then
        assertNotNull(savedAdmin);
        assertEquals("testUser", savedAdmin.getUsername());
        verify(adminRepository, times(1)).save(admin);
    }
}
