package com.dzhenetl.diplom.service;

import com.dzhenetl.diplom.exception.StorageException;

public class StorageFileNotFoundException extends StorageException {


    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
