package com.dzhenetl.diplom.service;

import com.dzhenetl.diplom.entity.File;
import com.dzhenetl.diplom.repository.FileRepository;
import com.dzhenetl.diplom.security.domain.User;
import com.dzhenetl.diplom.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    public void storeFile(MultipartFile file) throws AuthenticationException, IOException {
        String login = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByLogin(login).orElseThrow(AuthenticationException::new);
        File fileToSave = File.builder()
                .user(user)
                .name(file.getResource().getFilename())
                .data(file.getBytes())
                .build();
        fileRepository.save(fileToSave);
    }

    public void deleteFile(String filename) {
        fileRepository.deleteByName(filename);
    }
}
