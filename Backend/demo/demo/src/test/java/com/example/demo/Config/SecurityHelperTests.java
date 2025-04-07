package com.example.demo.Config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SecurityHelperTests {

    private SecurityHelper securityHelper = new SecurityHelper();

    @Test
    public void testHashPassword() {
        String input = "test";
        String expected = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
        String result = securityHelper.hashPassword(input);
        assertEquals(expected, result);
    }
}
