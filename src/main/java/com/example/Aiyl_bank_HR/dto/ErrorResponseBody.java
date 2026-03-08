package com.example.Aiyl_bank_HR.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ErrorResponseBody {
    private String title;
    private Map<String, List<String>> response;
}
