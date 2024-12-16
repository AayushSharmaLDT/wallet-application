package com.wallet_app.response;

import lombok.Getter;

@Getter
public class Response<T> {
    private String message;
    private T data;

    public Response(String message, T data) {
        this.message = message;
        this.data = data;
    }

}