package com.example.bankCard.handleResponse;

import com.example.bankCard.model.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class HandleResponse {
    private String message;
    private int code;
    private Card card;

    public HandleResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public HandleResponse(int code, Card card) {
        this.code = code;
        this.card = card;
    }
}
