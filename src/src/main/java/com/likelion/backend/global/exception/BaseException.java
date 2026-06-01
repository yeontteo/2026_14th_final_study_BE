package com.likelion.backend.global.exception;

import com.likelion.backend.global.response.ErrorStatus;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ErrorStatus errorStatus;

    public BaseException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public BaseException(ErrorStatus errorStatus, Throwable cause) {
        super(errorStatus.getMessage(), cause);
        this.errorStatus = errorStatus;
    }

    public int getStatusCode() {
        return this.errorStatus.getStatusCode();
    }
}
