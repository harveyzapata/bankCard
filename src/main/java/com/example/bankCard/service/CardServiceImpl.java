package com.example.bankCard.service;

import com.example.bankCard.handleResponse.HandleResponse;
import com.example.bankCard.model.Card;
import com.example.bankCard.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Random;


@Service
public class CardServiceImpl implements  CardService{
    @Autowired
    CardRepository cardRepository;

    public HandleResponse generateCardNumber(Long productId) {
            var optionalTicket = cardRepository.findById(productId);
            if( optionalTicket.isEmpty()) return new HandleResponse("Not found Card", 404);
            Random random = new Random();
            String productIdStr = String.valueOf(productId);
            int longitud = Math.min(productIdStr.length(), 6);
            String primeros6Digitos = productIdStr.substring(0, longitud);
            while (primeros6Digitos.length() < 6) {
                primeros6Digitos = "0" + primeros6Digitos;
            }
            StringBuilder cardNumberBuilder = new StringBuilder(primeros6Digitos);
            for (int i = 0; i < 9; i++) {
                cardNumberBuilder.append(random.nextInt(10));
            }
            String cardNumber = cardNumberBuilder.toString();
            cardNumber = cardNumber + calculateLuhnDigit(cardNumber);
            Card card = new Card();
            card.setCardHolderName("John Doe");
            card.setCardId(cardNumber);
            card.setExpirationDate(LocalDate.now().plusYears(3)); // Vence en 3 años
            card.setCurrency("USD"); // Moneda en dólares
            card.setActive(false);
            card.setBalance(0);
            card.setBlocked(false);
            cardRepository.save(card);

            return new HandleResponse("Card number generate successfully",200, card);
    }

    public HandleResponse ActiveCard(String cardId) {
        Card optionalCard = cardRepository.findByCardId(cardId);
        if(optionalCard == null) return new HandleResponse("Not found Card", 404, null);
        if(optionalCard.isActive()) return new HandleResponse("The card is already active", 200);
        optionalCard.setActive(true);
        cardRepository.save(optionalCard);

        return new HandleResponse("Card was activate successfully",200, optionalCard);
    }

    public HandleResponse BlockCard(String cardId){
        Card card = cardRepository.findByCardId(cardId);
        if(card == null) return new HandleResponse("Not found", 404, null);
        card.setBlocked(true);
        cardRepository.save(card);

        return new HandleResponse("Card Blocked successfully", 200,card);
    }

    public HandleResponse AddBalance(String cardId, double balance){
        Card card = cardRepository.findByCardId(cardId);
        if(card == null) return new HandleResponse("Not found", 404, null);
        if(!card.isActive()) return new HandleResponse("The card is not already active", 200);
        if(card.isBlocked()) return new HandleResponse("The card its Blocked", 200);
        card.setBalance(balance);
        cardRepository.save(card);

        return new HandleResponse("Added balance successfully", 200,card);
    }

    public HandleResponse GetBalance(String CardId){
        Card card = cardRepository.findByCardId(CardId);
        if(card == null) return new HandleResponse("Not found", 404, null);
        if(card.isBlocked()) return new HandleResponse("The card its Blocked", 200, null);

        return new HandleResponse("Added balance successfully", 200, card);
    }

    private static int calculateLuhnDigit(String cardNumber) {
        int sum = 0;
        boolean doubleDigit = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));

            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }

        return (10 - (sum % 10)) % 10;
    }


}
