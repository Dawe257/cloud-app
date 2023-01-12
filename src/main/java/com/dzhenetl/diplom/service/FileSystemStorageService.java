package com.dzhenetl.diplom.service;

import com.dzhenetl.diplom.entity.File;
import com.dzhenetl.diplom.exception.StorageException;
import com.dzhenetl.diplom.repository.FileRepository;
import com.dzhenetl.diplom.security.domain.User;
import com.dzhenetl.diplom.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Autowired
    public FileSystemStorageService(StorageProperties properties, FileRepository fileRepository, UserRepository userRepository) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
            String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            File fileToStore = File.builder()
                    .path(destinationFile.toAbsolutePath().toString())
                    .user(userRepository.findByLogin(principal).orElseThrow())
                    .filename(destinationFile.getFileName().toString())
                    .size(file.getSize())
                    .build();
            fileRepository.save(fileToStore);
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    public List<File> list(int limit) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByLogin(principal).orElseThrow();
        return fileRepository.findByUserId(user.getId()).stream().limit(limit).collect(Collectors.toList());
    }

    public void delete(String filename) throws IOException {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByLogin(principal).orElseThrow();
        File fileToDelete = fileRepository.findByFilename(filename);
        if (user.getId().equals(fileToDelete.getUser().getId())) {
            fileRepository.delete(fileToDelete);
            Files.delete(Path.of(fileToDelete.getPath()));
        } else {
            throw new StorageException(principal + " is not owner file " + filename);
        }
    }


    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
