package com.likelion.backend.global.exception;

import com.likelion.backend.global.response.ErrorStatus;

public class ArticleNotFoundException extends BaseException {

    public ArticleNotFoundException(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}
