package com.dzhenetl.diplom.repository;

import com.dzhenetl.diplom.entity.File;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {
    void deleteByName(String filename);
}
