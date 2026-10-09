package com.kt.maven;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.kt.maven.repository.AppRepository;
import org.junit.jupiter.api.BeforeEach;

@Testcontainers
@SpringBootTest(properties = "spring.jpa.hibernate.ddl-auto=update")
@AutoConfigureMockMvc
class AppRecoIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppRepository repo;

    @BeforeEach
    void clearTestDatabase() {
        // Clears only the temporary test database.
        repo.deleteAll();
    }


    @Test
    void createdRecordCanBeRetrieved() throws Exception {
        mockMvc.perform(post("/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Integration test record"}
                                """))
                .andExpect(status().is2xxSuccessful());

        assertEquals(1L, repo.count());

        mockMvc.perform(get("/records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name")
                        .value("Integration test record"))
                .andExpect(jsonPath("$[0].id").isNumber());
    }

    @Test
    void emptyDatabaseReturnsEmptyList() throws Exception {
        mockMvc.perform(get("/records"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void malformedJsonIsRejectedWithoutSaving() throws Exception {
        mockMvc.perform(post("/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":"))
                .andExpect(status().isBadRequest());

        assertEquals(0L, repo.count());
    }
}