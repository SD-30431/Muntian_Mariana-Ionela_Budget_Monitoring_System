package com.example.demo.BusinessLogic;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByUsername() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(user);

        User result = userService.findByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testSave() {
        User user = new User();
        user.setUsername("testUser");

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.save(user);
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());

        verify(userRepository, times(1)).save(user);
    }
}
