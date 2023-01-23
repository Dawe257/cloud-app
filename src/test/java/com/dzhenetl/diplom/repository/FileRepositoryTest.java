package com.dzhenetl.diplom.repository;

import com.dzhenetl.diplom.entity.File;
import com.dzhenetl.diplom.security.domain.User;
import com.dzhenetl.diplom.util.BaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class FileRepositoryTest extends BaseTest {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 3; i++) {
            User user = User.builder()
                    .login("test_login" + i)
                    .password("test_password" + i)
                    .build();
            user = userRepository.save(user);
            File file = File.builder()
                    .filename("test.test" + i)
                    .path("/test/path" + i)
                    .size(Long.MAX_VALUE)
                    .user(user)
                    .build();
            fileRepository.save(file);
        }
    }

    @Test
    void findByUserId() {
        User expectedUser = User.builder()
                .login("test_login")
                .password("test_password")
                .build();
        expectedUser = userRepository.save(expectedUser);
        File expectedFile = File.builder()
                .filename("test.test")
                .path("/test/path")
                .size(Long.MAX_VALUE)
                .user(expectedUser)
                .build();
        expectedFile = fileRepository.save(expectedFile);

        List<File> actualFiles = fileRepository.findByUserId(expectedFile.getUser().getId());
        assertThat(actualFiles.size()).isEqualTo(1);
        assertThat(actualFiles).containsOnly(expectedFile);
        assertThat(actualFiles.get(0).getUser()).isEqualTo(expectedUser);
    }

    @Test
    void findByFilename() {
        User expectedUser = User.builder()
                .login("test_login")
                .password("test_password")
                .build();
        expectedUser = userRepository.save(expectedUser);
        File expectedFile = File.builder()
                .filename("test.test")
                .path("/test/path")
                .size(Long.MAX_VALUE)
                .user(expectedUser)
                .build();
        expectedFile = fileRepository.save(expectedFile);

        File actualFile = fileRepository.findByFilename(expectedFile.getFilename());
        assertThat(actualFile).isEqualTo(expectedFile);
        assertThat(actualFile.getUser()).isEqualTo(expectedUser);
    }

    @AfterEach
    void clean() {
        fileRepository.deleteAll();
        userRepository.deleteAll();
    }
}