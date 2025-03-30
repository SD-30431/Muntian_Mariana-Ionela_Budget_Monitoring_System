package com.example.demo.Controllers;

import com.example.demo.BusinessLogic.UserService;
import com.example.demo.Config.SecurityHelper;
import com.example.demo.Model.User;
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

@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private SecurityHelper securityHelper;

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        user.setPasswordHash("plainPassword");
        String hashedPassword = "hashedValue";
        when(securityHelper.hashPassword("plainPassword")).thenReturn(hashedPassword);
        user.setPasswordHash(hashedPassword);
        when(userService.save(any(User.class))).thenReturn(user);
        String userJson = "{\"username\":\"testUser\",\"passwordHash\":\"plainPassword\"}";
        mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.passwordHash").value(hashedPassword));
    }

    @Test
    public void testGetUser() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        user.setPasswordHash("hashedValue");
        when(userService.findByUsername("testUser")).thenReturn(user);
        mockMvc.perform(get("/user/testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.passwordHash").value("hashedValue"));
    }
}
