package com.example.bankCard.service;

import com.example.bankCard.handleResponse.HandleResponseTransaction;


public interface CardTransaction {
    HandleResponseTransaction Purchase(String cardId, double price);
    HandleResponseTransaction GetTransaction(Long TransactionId);
    HandleResponseTransaction CancelTransaction(String CardId, Long Id);
}
