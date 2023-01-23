package com.dzhenetl.diplom.service;

import com.dzhenetl.diplom.repository.UserRepository;
import com.dzhenetl.diplom.security.domain.User;
import com.dzhenetl.diplom.util.BaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceTest extends BaseTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

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
