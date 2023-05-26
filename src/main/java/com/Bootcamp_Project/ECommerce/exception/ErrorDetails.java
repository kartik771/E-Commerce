package com.Bootcamp_Project.ECommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorDetails {

    private LocalDate timestamp;
    private String message;
    private String details;


}
