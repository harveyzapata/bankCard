package com.example.bankCard.repository;

import com.example.bankCard.model.Card;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;



@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Card findByCardId(String cardId);

}

