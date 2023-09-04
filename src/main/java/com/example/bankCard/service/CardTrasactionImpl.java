package com.example.bankCard.service;

import com.example.bankCard.handleResponse.HandleResponseTransaction;
import com.example.bankCard.model.Card;
import com.example.bankCard.model.Transaction;
import com.example.bankCard.repository.CardRepository;
import com.example.bankCard.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        if(card == null) return new HandleResponseTransaction("Card not found", 404, null);
        if(!card.isActive()) return new HandleResponseTransaction("The card is not already active", 200,null);
        if(card.isBlocked()) return new HandleResponseTransaction("The card its Blocked", 200, null);
        if(card.getBalance()< Price) return new HandleResponseTransaction("You don't have enough balance", 200,null);

        Transaction transaction = new Transaction();
        transaction.setCardNumber(CardId);
        transaction.setPrice(Price);
        transaction.setTransactionDate(LocalDateTime.now());

        double newBalance = card.getBalance() - Price;
        if (newBalance >= 0) {
            card.setBalance(newBalance);
            cardRepository.save(card);
            transactionRepository.save(transaction);
        }

        return new HandleResponseTransaction("Purchase successfully", 200, transaction);
    }

    public HandleResponseTransaction GetTransaction(Long transactionId){
        var optionalTransaction = transactionRepository.findById(transactionId);
        if( optionalTransaction.isEmpty()) return new HandleResponseTransaction("Not found Transaction", 404);

        Transaction transaction = optionalTransaction.get();

        return new HandleResponseTransaction("The information is", 200, transaction);

    }

    public HandleResponseTransaction CancelTransaction(String CardId, Long Id){
        var optransaction = transactionRepository.findById(Id);

        if(optransaction.isEmpty()) return new HandleResponseTransaction("Transaction not found ", 404);

        Transaction transaction = optransaction.get();
        LocalDateTime now = LocalDateTime.now();
        long hoursDifference = ChronoUnit.HOURS.between(transaction.getTransactionDate(), now);

        if(hoursDifference>24) return new HandleResponseTransaction("The transaction cannot be canceled and more than 24 hours have passed", 200, null);

        Card card = cardRepository.findByCardId(CardId);
        if(card == null) return new HandleResponseTransaction("Card not found", 404, null);

        double newBalance = card.getBalance() + transaction.getPrice();
        card.setBalance(newBalance);
        transaction.setCanceled(true);
        cardRepository.save(card);
        transactionRepository.save(transaction);

        return new HandleResponseTransaction("Transaction canceled", 200, transaction);
    }


}
