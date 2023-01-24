package com.dzhenetl.diplom.service;

import com.dzhenetl.diplom.repository.UserRepository;
import com.dzhenetl.diplom.security.domain.User;
import com.dzhenetl.diplom.util.TestcontainersDbUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = TestcontainersDbUtils.createPostgreSQLContainer();

    @DynamicPropertySource
    static void setLiquibaseChangeLog(DynamicPropertyRegistry propertyRegistry) {
        TestcontainersDbUtils.setDatasourceProperties(propertyRegistry, postgreSQLContainer);
    }

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 3; i++) {
            User user = User.builder()
                    .login("test_login" + i)
                    .password("test_password" + i)
                    .build();
            userRepository.save(user);
        }
    }

    @Test
    void getByLogin() {
        User expectedUser = User.builder()
                .login("test_login")
                .password("test_password")
                .build();
        expectedUser = userRepository.save(expectedUser);

        Optional<User> actualUser = userService.getByLogin(expectedUser.getLogin());
        assertThat(actualUser).isPresent();
        assertThat(actualUser).get().isEqualTo(expectedUser);
    }

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }
}
