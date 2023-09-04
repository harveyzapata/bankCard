package com.example.bankCard.model;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cards")

public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "card_number", unique = true, nullable = false)
    private String cardId;

    @Column(name = "card_holder_name", nullable = false)
    private String cardHolderName;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "currency", nullable = false)
    private String currency; // Permite únicamente movimiento en dólares.

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "balance", nullable = false)
    private double balance;

    @Column(name = "block", nullable = false)
    private boolean Blocked;

}

