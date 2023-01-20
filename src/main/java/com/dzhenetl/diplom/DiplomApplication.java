package com.dzhenetl.diplom;

import com.dzhenetl.diplom.repository.UserRepository;
import com.dzhenetl.diplom.security.domain.User;
import com.dzhenetl.diplom.service.StorageProperties;
import com.dzhenetl.diplom.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class DiplomApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiplomApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService, UserRepository userRepository) {
        User user = userRepository.findByLoginAndPassword("ivan", "123")
                .orElse(User.builder()
                        .login("ivan")
                        .password("123")
                        .build()
                );
        userRepository.save(user);
        return (args) -> {
//            storageService.deleteAll();
            storageService.init();
        };
    }
}
