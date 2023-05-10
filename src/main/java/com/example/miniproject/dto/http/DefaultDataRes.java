package com.example.miniproject.dto.http;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultDataRes<T> extends DefaultRes<T> {

    private T data;


    public DefaultDataRes(String responseMessage, T data) {
        super(responseMessage);
        this.data = data;
    }

//    public static <T> DefaultDataRes<T> dataRes(final int statusCode, final String responseMessage, T data) {
//        return (DefaultDataRes<T>) dataRes(statusCode, responseMessage, data);
//    }
}
