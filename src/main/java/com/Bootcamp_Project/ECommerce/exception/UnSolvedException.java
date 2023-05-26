package com.Bootcamp_Project.ECommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UnSolvedException extends Exception {

    public UnSolvedException(String message)
    {
        super(message);
    }
}
