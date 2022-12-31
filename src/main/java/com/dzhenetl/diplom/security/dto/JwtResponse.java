package com.dzhenetl.diplom.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponse {

    @JsonProperty("auth-token")
    private String authToken;
}
