package com.example.demo.BusinessLogic;

import com.example.demo.Model.Budget;
import com.example.demo.Repository.BudgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BudgetServiceTests {

    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private BudgetService budgetService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByCardNumber() {
        // Given
        String cardNumber = "1234-5678-9012-3456";
        Budget budget = new Budget();
        // Assume Budget has a method to set the card number
        budget.setCardnumber(cardNumber);

        // When the repository is called, return the budget instance
        when(budgetRepository.findByCardnumber(cardNumber)).thenReturn(budget);

        // Execute
        Budget result = budgetService.findByCardNumber(cardNumber);

        // Then
        assertNotNull(result);
        assertEquals(cardNumber, result.getCardnumber());
        verify(budgetRepository, times(1)).findByCardnumber(cardNumber);
    }

    @Test
    public void testSave() {
        // Given
        Budget budget = new Budget();
        budget.setCardnumber("1234-5678-9012-3456");

        // When saving, simulate the repository returning the same budget instance
        when(budgetRepository.save(budget)).thenReturn(budget);

        // Execute
        Budget savedBudget = budgetService.save(budget);

        // Then
        assertNotNull(savedBudget);
        assertEquals("1234-5678-9012-3456", savedBudget.getCardnumber());
        verify(budgetRepository, times(1)).save(budget);
    }
}
