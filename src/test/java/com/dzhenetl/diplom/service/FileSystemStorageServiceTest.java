package com.dzhenetl.diplom.service;

import com.dzhenetl.diplom.entity.File;
import com.dzhenetl.diplom.exception.StorageException;
import com.dzhenetl.diplom.repository.FileRepository;
import com.dzhenetl.diplom.repository.UserRepository;
import com.dzhenetl.diplom.security.domain.User;
import com.dzhenetl.diplom.util.TestcontainersDbUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class FileSystemStorageServiceTest {

    @TempDir(cleanup = CleanupMode.ALWAYS)
    private static Path tempDir;
    private final StorageService storageService;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = TestcontainersDbUtils.createPostgreSQLContainer();

    @DynamicPropertySource
    static void setLiquibaseChangeLog(DynamicPropertyRegistry propertyRegistry) {
        TestcontainersDbUtils.setDatasourceProperties(propertyRegistry, postgreSQLContainer);
    }

    @Autowired
    public FileSystemStorageServiceTest(FileRepository fileRepository, UserRepository userRepository) {
        StorageProperties properties = new StorageProperties();
        properties.setLocation(tempDir.toString());
        properties.setAllowStoringOutsideCurrentDirectory(true);
        this.storageService = new FileSystemStorageService(properties, fileRepository, userRepository);
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    @Test
    @WithMockUser(username = "ivan", password = "123", authorities = "ADMIN, USER")
    void shouldThrowException() throws Exception {
        Path createdFile = Files.createFile(
                tempDir.resolve("createdFile.txt")
        );
        MultipartFile multipartFile = new MockMultipartFile(
                createdFile.getFileName().toString(),
                createdFile.getFileName().toString(),
                MediaType.APPLICATION_PDF_VALUE,
                new FileInputStream(createdFile.toString())
        );
        Files.delete(createdFile);
        assertThrows(
                StorageException.class,
                () -> storageService.store(multipartFile)
        );
    }

    @Test
    @WithMockUser(username = "ivan", password = "123", authorities = "ADMIN, USER")
    void store() throws IOException {
        Path createdFile = Files.createFile(
                tempDir.resolve("createdFile.txt")
        );
        try (FileWriter writer = new FileWriter(createdFile.toFile())) {
            writer.write(1);
        }
        MultipartFile multipartFile = new MockMultipartFile(
                createdFile.getFileName().toString(),
                createdFile.getFileName().toString(),
                MediaType.APPLICATION_PDF_VALUE,
                new FileInputStream(createdFile.toString())
        );
        Files.delete(createdFile);
        storageService.store(multipartFile);

        List<Path> actualFiles = Files.list(tempDir).collect(Collectors.toList());
        assertThat(actualFiles).isNotNull();
        assertThat(actualFiles.size()).isEqualTo(1);
        assertThat(actualFiles).containsOnly(createdFile);
    }

    @Test
    @WithMockUser(username = "ivan", password = "123", authorities = "ADMIN, USER")
    void list() {
        User owner = userRepository.findByLogin("ivan").get();
        List<File> ownerFiles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            File file = File.builder()
                    .filename("test.test1" + i)
                    .path("/test/path1" + i)
                    .size(Long.MAX_VALUE)
                    .user(owner)
                    .build();
            ownerFiles.add(fileRepository.save(file));
        }
        User notOwner = User.builder()
                .login("test_login2")
                .password("test_password2")
                .build();
        notOwner = userRepository.save(notOwner);
        List<File> notOwnerFiles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            File file = File.builder()
                    .filename("test.test2" + i)
                    .path("/test/path2" + i)
                    .size(Long.MAX_VALUE)
                    .user(notOwner)
                    .build();
            notOwnerFiles.add(fileRepository.save(file));
        }

        List<File> actualFiles = storageService.list(10);

        assertThat(actualFiles.size()).isEqualTo(3);
        assertThat(actualFiles).containsOnlyOnceElementsOf(ownerFiles);
    }

    @Test
    @WithMockUser(username = "ivan", password = "123", authorities = "ADMIN, USER")
    void delete() throws IOException {
        Path createdFile = Files.createFile(
                tempDir.resolve("createdFile.txt")
        );
        User ivan = userRepository.findByLogin("ivan").get();
        File createdFileEntity = fileRepository.save(
                File.builder()
                        .user(ivan)
                        .path(createdFile.toAbsolutePath().toString())
                        .size(createdFile.toFile().length())
                        .filename(createdFile.getFileName().toString())
                        .build()
        );

        storageService.delete(createdFile.getFileName().toString());
        assertThat(fileRepository.findByFilename(createdFileEntity.getFilename())).isNull();
        assertThat(Files.exists(createdFile)).isFalse();

        User petya = userRepository.save(User.builder()
                .login("petya")
                .build());
        createdFile = Files.createFile(
                tempDir.resolve("createdFile.txt")
        );
        createdFileEntity = fileRepository.save(
                File.builder()
                        .user(petya)
                        .path(createdFile.toAbsolutePath().toString())
                        .size(createdFile.toFile().length())
                        .filename(createdFile.getFileName().toString())
                        .build()
        );
        String filename = createdFileEntity.getFilename();
        assertThrows(StorageException.class, () -> storageService.delete(filename));
    }

    @AfterEach
    void clean() throws IOException {
        Files.deleteIfExists(
                tempDir.resolve("createdFile.txt")
        );
    }
}