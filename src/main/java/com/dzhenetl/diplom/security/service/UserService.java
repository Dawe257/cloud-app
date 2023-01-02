package com.dzhenetl.diplom.security.service;

import com.dzhenetl.diplom.security.domain.User;
import com.dzhenetl.diplom.security.repository.UserRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> getByLogin(@NonNull String login) {
        return repository.findByLogin(login);
    }
}
