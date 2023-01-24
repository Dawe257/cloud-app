package com.dzhenetl.diplom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    @JsonProperty("auth-token")
    private String authToken;
}
