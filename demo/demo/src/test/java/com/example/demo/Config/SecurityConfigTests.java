package com.example.demo.Config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTests {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class DummyControllerConfig {
        @RestController
        static class DummyController {
            @GetMapping("/dummy")
            public String dummy() {
                return "dummy";
            }
        }
    }

    @Test
    public void testPermitAll() throws Exception {
        mockMvc.perform(get("/dummy"))
                .andExpect(status().isOk());
    }
}
