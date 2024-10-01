package com.Hindol.Uber.Advice;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class APIError {
    private HttpStatus httpStatus;
    private String message;
    private List<String> subErrors;
}
