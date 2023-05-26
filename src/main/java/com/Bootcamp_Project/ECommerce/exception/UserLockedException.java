package com.Bootcamp_Project.ECommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.LOCKED)
public class UserLockedException extends RuntimeException{
    public UserLockedException(String message) {
        super(message);
    }
}
