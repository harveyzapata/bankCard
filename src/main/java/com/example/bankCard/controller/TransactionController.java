package com.example.bankCard.controller;
import com.example.bankCard.handleResponse.HandleResponseTransaction;
import com.example.bankCard.service.CardTransaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final CardTransaction cardTransaction;

    public TransactionController(CardTransaction  cardTransaction ) {
        this.cardTransaction = cardTransaction;
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Object> GetTransaction(@PathVariable Long transactionId) {
        HandleResponseTransaction transaction = cardTransaction.GetTransaction(transactionId);
        return ResponseEntity.status(transaction.getCode()).body(transaction);

    }
    @PostMapping("/purchase")
    public ResponseEntity<Object> PurchaseTransaction(@RequestBody Map<String, Object> requestMap) {
        String cardId = (String) requestMap.get("cardId");
        double price = (double) requestMap.get("price");
        HandleResponseTransaction response = cardTransaction.Purchase(cardId, price);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/anulation")
    public ResponseEntity<Object> AnulationTransaction(@RequestBody Map<String, Object> requestMap) {
        String cardId = (String) requestMap.get("cardId");
        String vatransactionId = (String) requestMap.get("transactionId");
        Long transactionId = Long.parseLong(vatransactionId);
        HandleResponseTransaction response = cardTransaction.CancelTransaction(cardId, transactionId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

}
