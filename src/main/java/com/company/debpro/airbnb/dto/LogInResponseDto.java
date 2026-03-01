package com.company.debpro.airbnb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class LogInResponseDto {
    private String accessToken;
}
