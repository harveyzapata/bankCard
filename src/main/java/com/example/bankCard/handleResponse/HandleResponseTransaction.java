package com.example.bankCard.handleResponse;

import com.example.bankCard.model.Card;
import com.example.bankCard.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class HandleResponseTransaction {
    private String message;
    private int code;
    private Transaction transaction;

    public HandleResponseTransaction(String message, int code) {
        this.message = message;
        this.code = code;
    }

}
