package com.dzhenetl.diplom.advice;

import com.dzhenetl.diplom.dto.ErrorMessageDto;
import com.dzhenetl.diplom.exception.AuthException;
import com.dzhenetl.diplom.exception.StorageException;
import com.dzhenetl.diplom.exception.StorageFileNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({AuthException.class, StorageException.class, StorageFileNotFoundException.class})
    public ErrorMessageDto handle(Exception e) {
        return new ErrorMessageDto(e.getMessage());
    }
}
