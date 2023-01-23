package com.dzhenetl.diplom.repository;

import com.dzhenetl.diplom.entity.File;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {

    List<File> findByUserId(Long id);
    // TODO Optional
    File findByFilename(String filename);
}
