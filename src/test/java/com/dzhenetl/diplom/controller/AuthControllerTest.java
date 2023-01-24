package com.dzhenetl.diplom.controller;

import com.dzhenetl.diplom.confroller.AuthController;
import com.dzhenetl.diplom.dto.JwtRequest;
import com.dzhenetl.diplom.dto.JwtResponse;
import com.dzhenetl.diplom.util.TestcontainersDbUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = TestcontainersDbUtils.createPostgreSQLContainer();

    @DynamicPropertySource
    static void setLiquibaseChangeLog(DynamicPropertyRegistry propertyRegistry) {
        TestcontainersDbUtils.setDatasourceProperties(propertyRegistry, postgreSQLContainer);
    }

    @Test
    void login() {
        String url = "http://localhost:" + port + AuthController.LOGIN_URL;
        JwtRequest request = new JwtRequest();
        request.setLogin("ivan");
        request.setPassword("123");
        ResponseEntity<JwtResponse> jwtResponse = restTemplate.postForEntity(url, request, JwtResponse.class);
        assertThat(jwtResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        request.setPassword("12345");
        jwtResponse = restTemplate.postForEntity(url, request, JwtResponse.class);
        assertThat(jwtResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
