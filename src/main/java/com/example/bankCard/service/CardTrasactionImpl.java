package com.example.bankCard.service;

import com.example.bankCard.handleResponse.HandleResponse;
import com.example.bankCard.handleResponse.HandleResponseTransaction;
import com.example.bankCard.model.Card;
import com.example.bankCard.model.Transaction;
import com.example.bankCard.repository.CardRepository;
import com.example.bankCard.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class CardTrasactionImpl implements CardTransaction{

    @Autowired
    public CardRepository cardRepository;
    @Autowired
     TransactionRepository transactionRepository;
    public HandleResponseTransaction Purchase(String CardId, double Price ){
        Card card = cardRepository.findByCardId(CardId);
        if(card == null){
            HandleResponseTransaction notFoundResponse = new HandleResponseTransaction("Card not found", 404, null);
            return notFoundResponse;
        }
        if(!card.isActive()){
            HandleResponseTransaction response = new HandleResponseTransaction("The card is not already active", 200,null);
            return response;
        }
        if(card.isBlocked()){
            HandleResponseTransaction response = new HandleResponseTransaction("The card its Blocked", 200, null);
            return response;
        }
        if(card.getBalance()< Price){
            HandleResponseTransaction response = new HandleResponseTransaction("You don't have enough balance", 200,null);
            return response;
        }
        Transaction transaction = new Transaction();
        transaction.setCardNumber(CardId);
        transaction.setPrice(Price);
        transaction.setTransactionDate(LocalDateTime.now());

        Double newBalance = card.getBalance() - Price;
        if (newBalance >= 0) {
            card.setBalance(newBalance);
            cardRepository.save(card);
            transactionRepository.save(transaction);
        }

        HandleResponseTransaction response = new HandleResponseTransaction("Purchase successfully", 200, transaction);
        return response;
    }

    public HandleResponseTransaction GetTransaction(Long transactionId){
        var optionalTransaction = transactionRepository.findById(transactionId);
        if( optionalTransaction.isEmpty()){
            HandleResponseTransaction notFoundResponse = new HandleResponseTransaction("Not found Transaction", 404);
            return notFoundResponse;
        }
        Transaction transaction = optionalTransaction.get();
        HandleResponseTransaction response = new HandleResponseTransaction("The information is", 200, transaction);
        return response;

    }

    public HandleResponseTransaction CancelTransaction(String CardId, Long Id){
        var optransaction = transactionRepository.findById(Id);
        if(optransaction.isEmpty()){
            HandleResponseTransaction notFoundResponse = new HandleResponseTransaction("Transaction not found ", 404);
            return notFoundResponse;
        }
        Transaction transaction = optransaction.get();
        LocalDateTime now = LocalDateTime.now();
        long hoursDifference = ChronoUnit.HOURS.between(transaction.getTransactionDate(), now);
        if(hoursDifference>24){
            HandleResponseTransaction response = new HandleResponseTransaction("The transaction cannot be canceled and more than 24 hours have passed", 200, null);
            return response;
        }
        Card card = cardRepository.findByCardId(CardId);
        if(card == null){
            HandleResponseTransaction notFoundResponse = new HandleResponseTransaction("Card not found", 404, null);
            return notFoundResponse;
        }
        Double newBalance = card.getBalance() + transaction.getPrice();
        card.setBalance(newBalance);
        transaction.setCanceled(true);
        cardRepository.save(card);
        transactionRepository.save(transaction);
        HandleResponseTransaction response = new HandleResponseTransaction("Transaction canceled", 200, transaction);
        return response;
    }


}
