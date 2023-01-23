package com.dzhenetl.diplom.util;


import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;

public class TestcontainersDbUtils {

    public static PostgreSQLContainer<?> createPostgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("diplom")
                .withUsername("test_user")
                .withPassword("test_password");
    }

    public static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry,
                                               PostgreSQLContainer<?> postgreSQLContainer) {
        propertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        propertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        propertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);

//        propertyRegistry.add("spring.liquibase.url", postgreSQLContainer::getJdbcUrl);
//        propertyRegistry.add("spring.liquibase.user", postgreSQLContainer::getUsername);
//        propertyRegistry.add("spring.liquibase.password", postgreSQLContainer::getPassword);
    }
}
