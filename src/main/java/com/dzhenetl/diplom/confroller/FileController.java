package com.dzhenetl.diplom.confroller;

import com.dzhenetl.diplom.service.StorageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/cloud/file")
@Transactional
public class FileController {

    private final StorageService service;

    @Autowired
    public FileController(StorageService storageService) {
        this.service = storageService;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    void uploadFile(MultipartFile file) {
        service.store(file);
    }

    @DeleteMapping("/{filename}")
    void deleteFile(@PathVariable String filename) throws IOException {
        service.delete(filename);
    }

    @GetMapping
    void listOfFiles() {
        System.out.println();
    }
}
