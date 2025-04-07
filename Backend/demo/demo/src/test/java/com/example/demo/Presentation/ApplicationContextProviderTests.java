package com.example.demo.Presentation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ApplicationContextProviderTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testContextProvider() {
        assertNotNull(ApplicationContextProvider.getContext());
        assertEquals(applicationContext, ApplicationContextProvider.getContext());
    }
}
