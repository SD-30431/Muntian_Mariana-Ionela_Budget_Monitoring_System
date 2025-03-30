package com.example.demo.Controllers;

import com.example.demo.BusinessLogic.BudgetService;
import com.example.demo.Model.Budget;
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

@WebMvcTest(BudgetController.class)
public class BudgetControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BudgetService budgetService;

    @Test
    public void testCreateBudget() throws Exception {
        Budget budget = new Budget();
        budget.setCardnumber("1234-5678-9012-3456");
        when(budgetService.save(any(Budget.class))).thenReturn(budget);
        String budgetJson = "{\"cardnumber\": \"1234-5678-9012-3456\"}";
        mockMvc.perform(post("/budget/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(budgetJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardnumber").value("1234-5678-9012-3456"));
    }

    @Test
    public void testGetBudget() throws Exception {
        Budget budget = new Budget();
        budget.setCardnumber("1234-5678-9012-3456");
        when(budgetService.findByCardNumber("1234-5678-9012-3456")).thenReturn(budget);
        mockMvc.perform(get("/budget/1234-5678-9012-3456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardnumber").value("1234-5678-9012-3456"));
    }
}
