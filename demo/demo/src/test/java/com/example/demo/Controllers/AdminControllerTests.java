package com.example.demo.Controllers;

import com.example.demo.BusinessLogic.AdminService;
import com.example.demo.Model.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
public class AdminControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Test
    public void testCreateAdmin() throws Exception {
        Admin admin = new Admin();
        admin.setUsername("testAdmin");
        when(adminService.save(any(Admin.class))).thenReturn(admin);
        String adminJson = "{\"username\": \"testAdmin\"}";
        mockMvc.perform(post("/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adminJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testAdmin"));
    }

    @Test
    public void testGetAdmin() throws Exception {
        Admin admin = new Admin();
        admin.setUsername("testAdmin");
        when(adminService.findByUsername("testAdmin")).thenReturn(admin);
        mockMvc.perform(get("/admin/testAdmin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testAdmin"));
    }
}
