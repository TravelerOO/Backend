package com.example.miniproject.dto.http;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultRes<T> {

    private int statusCode;
    private String responseMessage;

    public DefaultRes(final int statusCode, final String responseMessage) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
    }

    public static <T> DefaultRes<T> res(final int statusCode, final String responseMessage) {
        return res(statusCode, responseMessage);
    }

}

