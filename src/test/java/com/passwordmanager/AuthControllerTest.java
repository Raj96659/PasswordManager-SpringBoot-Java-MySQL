package com.passwordmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testLoginEndpoint() throws Exception {

        String json = """
        {
          "username": "raj123",
          "masterPassword": "Recovered@789"
        }
        """;

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk());
    }
}
