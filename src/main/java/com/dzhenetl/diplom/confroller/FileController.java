package com.dzhenetl.diplom.confroller;

import com.dzhenetl.diplom.entity.File;
import com.dzhenetl.diplom.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class FileController {

    private final StorageService service;

    @Autowired
    public FileController(StorageService storageService) {
        this.service = storageService;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, value = "/file")
    void uploadFile(MultipartFile file) {
        service.store(file);
    }

    @GetMapping("/file")
    ResponseEntity<Resource> getFile(@RequestParam String filename) {
        Resource file = service.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @DeleteMapping("/file")
    void deleteFile(@RequestParam String filename) throws IOException {
        service.delete(filename);
    }

    @GetMapping("/list")
    List<File> list(@RequestParam(defaultValue = "10") int limit) {
        return service.list(limit);
    }

    @GetMapping
    void listOfFiles() {
        System.out.println();
    }
}
