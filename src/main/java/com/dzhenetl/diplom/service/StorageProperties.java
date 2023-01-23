package com.dzhenetl.diplom.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("storage")
public class StorageProperties {

    private String location = "upload-dir";
    private boolean allowStoringOutsideCurrentDirectory = false;
}