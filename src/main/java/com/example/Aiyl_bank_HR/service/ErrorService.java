package com.example.Aiyl_bank_HR.service;

import com.example.Aiyl_bank_HR.dto.ErrorResponseBody;
import org.springframework.web.bind.MethodArgumentNotValidException;

public interface ErrorService {
    ErrorResponseBody makeResponse(Exception e);

    ErrorResponseBody makeResponse(MethodArgumentNotValidException e);
}
