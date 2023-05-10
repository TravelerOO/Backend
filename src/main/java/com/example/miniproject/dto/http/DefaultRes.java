package com.example.miniproject.dto.http;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultRes<T> {
    private String responseMessage;

    public DefaultRes(final String responseMessage) {
        this.responseMessage = responseMessage;
    }

//    public static <T> DefaultRes<T> res(final int statusCode, final String responseMessage) {
//        return res(statusCode, responseMessage);
//    }

}

