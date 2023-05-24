package com.nanoka.examen.dtos;

import lombok.Data;

@Data
public class DtoAuthResponse {
    private String token;

    public DtoAuthResponse(String token) {
        this.token = token;
    }
}
