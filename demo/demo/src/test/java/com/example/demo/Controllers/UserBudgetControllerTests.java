package com.example.demo.Controllers;

import com.example.demo.BusinessLogic.UserBudgetService;
import com.example.demo.BusinessLogic.UserService;
import com.example.demo.BusinessLogic.BudgetService;
import com.example.demo.Model.UserBudget;
import com.example.demo.Model.User;
import com.example.demo.Model.Budget;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(UserBudgetController.class)
public class UserBudgetControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserBudgetService userBudgetService;

    @MockBean
    private UserService userService;

    @MockBean
    private BudgetService budgetService;

    @Test
    public void testLinkUserBudget_Success() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        Budget budget = new Budget();
        budget.setCardnumber("1234-5678-9012-3456");
        UserBudget userBudget = new UserBudget(user, budget);
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(budgetService.findByCardNumber("1234-5678-9012-3456")).thenReturn(budget);
        when(userBudgetService.linkUserToBudget(user, budget)).thenReturn(userBudget);
        mockMvc.perform(post("/userbudget/link")
                        .param("username", "testUser")
                        .param("cardnumber", "1234-5678-9012-3456")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value("testUser"))
                .andExpect(jsonPath("$.budget.cardnumber").value("1234-5678-9012-3456"));
    }

    @Test
    public void testLinkUserBudget_NullUser() throws Exception {
        when(userService.findByUsername("nonexistent")).thenReturn(null);
        mockMvc.perform(post("/userbudget/link")
                        .param("username", "nonexistent")
                        .param("cardnumber", "1234-5678-9012-3456")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void testLinkUserBudget_NullBudget() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(budgetService.findByCardNumber("invalid")).thenReturn(null);
        mockMvc.perform(post("/userbudget/link")
                        .param("username", "testUser")
                        .param("cardnumber", "invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
