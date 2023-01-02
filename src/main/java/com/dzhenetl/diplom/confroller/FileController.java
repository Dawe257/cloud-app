package com.dzhenetl.diplom.confroller;

import com.dzhenetl.diplom.service.FileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cloud/file")
@Transactional
public class FileController {

    private final FileService service;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    void uploadFile(MultipartFile file) throws AuthenticationException, IOException {
        service.storeFile(file);
    }

    @DeleteMapping("/{filename}")
    void deleteFile(@PathVariable String filename) {
        service.deleteFile(filename);
    }
}
