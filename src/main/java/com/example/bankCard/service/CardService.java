package com.example.bankCard.service;

import com.example.bankCard.handleResponse.HandleResponse;


public interface CardService {
    HandleResponse generateCardNumber(Long productId);
    HandleResponse ActiveCard(String cardId);
    HandleResponse BlockCard(String cardId);
    HandleResponse AddBalance(String cardId, double balance);
    HandleResponse GetBalance(String cardId);

}
