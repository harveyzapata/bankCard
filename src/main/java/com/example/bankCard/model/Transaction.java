package com.example.bankCard.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@ManyToOne
    @JoinColumn(name = "card_number", referencedColumnName = "card_number", nullable = false)
    private Card card;*/
    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "canceled", nullable = false,  columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean canceled;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

}
