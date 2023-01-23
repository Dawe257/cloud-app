package com.dzhenetl.diplom.service;

import com.dzhenetl.diplom.security.domain.User;
import com.dzhenetl.diplom.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public Optional<User> getByLogin(@NonNull String login) {
        return repository.findByLogin(login);
    }
}
