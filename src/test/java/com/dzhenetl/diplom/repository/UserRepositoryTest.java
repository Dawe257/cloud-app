package com.dzhenetl.diplom.repository;

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

@Testcontainers
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

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
    void findByLogin() {
        User expectedUser = User.builder()
                .login("test_login")
                .password("test_password")
                .build();
        expectedUser = userRepository.save(expectedUser);

        Optional<User> actualUser = userRepository.findByLogin(expectedUser.getLogin());
        assertThat(actualUser).isPresent();
        assertThat(actualUser).get().isEqualTo(expectedUser);
    }

    @Test
    void findByLoginAndPassword() {
        User expectedUser = User.builder()
                .login("test_login")
                .password("test_password")
                .build();
        expectedUser = userRepository.save(expectedUser);

        Optional<User> actualUser = userRepository.findByLoginAndPassword(expectedUser.getLogin(), expectedUser.getPassword());
        assertThat(actualUser).isPresent();
        assertThat(actualUser).get().isEqualTo(expectedUser);
    }

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }
}
