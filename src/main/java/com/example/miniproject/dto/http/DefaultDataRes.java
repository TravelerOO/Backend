package com.example.miniproject.dto.http;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultDataRes<T> extends DefaultRes<T> {

    private T data;


    public DefaultDataRes(int statusCode, String responseMessage, T data) {
        super(statusCode, responseMessage);
        this.data = data;
    }

    public DefaultDataRes(int statusCode, String responseMessage) {
        super(statusCode, responseMessage);
    }

    public static <T> DefaultDataRes<T> dataRes(final int statusCode, final String responseMessage, T data) {
        return (DefaultDataRes<T>) dataRes(statusCode, responseMessage, data);
    }
}
