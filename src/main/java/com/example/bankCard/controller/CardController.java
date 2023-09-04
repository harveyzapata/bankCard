package com.example.bankCard.controller;

import com.example.bankCard.handleResponse.HandleResponse;
import com.example.bankCard.model.Card;

import com.example.bankCard.service.CardService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/card")

public class CardController {


    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }


    @GetMapping("/{productId}/number")

    public ResponseEntity<Object> generateCardNumber(@PathVariable Long productId) {
        HandleResponse card = cardService.generateCardNumber(productId);
        return ResponseEntity.status(card.getCode()).body(card);

    }

    @PostMapping("/enroll")
    public ResponseEntity<Object> ActivateCard(@RequestBody Card card) {
        HandleResponse updateActive = cardService.ActiveCard(card.getCardId());
        return ResponseEntity.status(updateActive.getCode()).body(updateActive);

    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Object> BlockCard(@PathVariable String cardId) {
        HandleResponse blockCard = cardService.BlockCard(cardId);
        return ResponseEntity.status(blockCard.getCode()).body(blockCard);
    }

    @PostMapping("/balance")
    public ResponseEntity<Object> AddBalance(@RequestBody Card card) {
        HandleResponse response = cardService.AddBalance(card.getCardId(), card.getBalance());
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/balance/{cardId}")
    public ResponseEntity<Object> GetBalance(@PathVariable String cardId) {
        HandleResponse response = cardService.GetBalance(cardId);
        if(response.getCard() != null){
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("balance", response.getCard().getBalance());
            return ResponseEntity.status(response.getCode()).body(responseMap);
        }
        return ResponseEntity.status(response.getCode()).body(response);
    }






}

