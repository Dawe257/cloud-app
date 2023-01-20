package com.dzhenetl.diplom.dto;

public class ErrorMessageDto {

    private static Integer id = 0;
    private final String message;

    public ErrorMessageDto(String message) {
        id++;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
