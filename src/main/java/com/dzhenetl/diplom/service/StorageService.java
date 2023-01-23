package com.dzhenetl.diplom.service;

import com.dzhenetl.diplom.entity.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StorageService {

    void init();

    void store(MultipartFile file);

    Resource loadAsResource(String filename);

    void delete(String filename) throws IOException;

    List<File> list(int limit);
}
