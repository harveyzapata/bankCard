package com.example.bankCard.service;

import com.example.bankCard.handleResponse.HandleResponse;
import com.example.bankCard.handleResponse.HandleResponseTransaction;
import com.example.bankCard.model.Card;
import com.example.bankCard.model.Transaction;
import com.example.bankCard.repository.CardRepository;
import com.example.bankCard.repository.TransactionRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private CardServiceImpl cardService;
    @InjectMocks
    private CardTrasactionImpl cardTransaction;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testGenerateCardNumber() {
        // Configurar comportamiento simulado del repositorio
        Long productId = 12345L;
        when(cardRepository.findById(productId)).thenReturn(Optional.of(new Card())); // Simula que el producto existe
        // Ejecutar el método que deseas probar
        HandleResponse response = cardService.generateCardNumber(productId);
        // Verificar que la respuesta no sea nula
        assertNotNull(response);
        // Verificar que la respuesta tenga un estado exitoso (código 200)
        assertEquals(200, response.getCode());
        // Verificar que la respuesta contenga una tarjeta generada
        assertNotNull(response.getCard());
        // Verificar que la tarjeta generada tenga un número de tarjeta válido
        Card generatedCard = (Card) response.getCard();
        assertNotNull(generatedCard.getCardId());

    }

    @Test
    void testActiveCard() {
        // Configurar comportamiento simulado
        Card card = new Card();
        card.setCardId("1234567890123456");
        card.setActive(false); // Asegúrate de que la tarjeta esté inactiva
        when(cardRepository.findByCardId("1234567890123456")).thenReturn(card);
        // Ejecutar el método que deseas probar
        cardService.ActiveCard("1234567890123456");
        // Verificar que la tarjeta ahora esté activa
        assertTrue(card.isActive());
    }
    @Test
    void testGetBalance() {
        // Configura un objeto Card simulado
        Card card = new Card();
        card.setCardId("1234567890123456");
        card.setBalance(1000.0);
        // Configura el comportamiento simulado del repositorio de tarjetas
        when(cardRepository.findByCardId("1234567890123456")).thenReturn(card);
        // Llama al método GetBalance
        double balance = cardService.GetBalance("1234567890123456").getCard().getBalance();
        // Verifica que se haya llamado a findByCardId con el número de tarjeta correcto
        verify(cardRepository).findByCardId("1234567890123456");
        // Verifica que el saldo devuelto sea el esperado
        assertEquals(1000.0, balance);
    }

    @Test
    void testBlockCard() {
        // Configurar comportamiento simulado
        Card card = new Card();
        card.setCardId("1234567890123456");
        card.setBlocked(false); // Asegúrate de que la tarjeta no esté bloqueada
        when(cardRepository.findByCardId("1234567890123456")).thenReturn(card);

        // Ejecutar el método que deseas probar
        cardService.BlockCard("1234567890123456");

        // Verificar que la tarjeta ahora esté bloqueada
        assertTrue(card.isBlocked());
    }

    @Test
    void testAddBalance() {
        // Configurar comportamiento simulado
        Card card = new Card();
        card.setCardId("1234567890123456");
        card.setBalance(1000.0);
        card.setActive(true);
        card.setBlocked(false);
        when(cardRepository.findByCardId("1234567890123456")).thenReturn(card);
        // Ejecutar el método que deseas probar
        HandleResponse response = cardService.AddBalance("1234567890123456", 500.0);
        // Verificar que se haya llamado a cardRepository.save(card)
        verify(cardRepository, times(1)).save(card);
        // Verificar el resultado de la respuesta
        assertEquals("Added balance successfully", response.getMessage());
        assertEquals(200, response.getCode());

    }

    @Test
    void testPurchaseTransaction() {
        // Configurar comportamiento simulado del repositorio de tarjetas
        String cardId = "1234567890123456";
        Card card = new Card();
        card.setCardId(cardId);
        card.setActive(true);
        card.setBlocked(false);
        card.setBalance(1000.0);
        when(cardRepository.findByCardId(cardId)).thenReturn(card);
        // Configurar comportamiento simulado del repositorio de transacciones
        Transaction transaction = new Transaction();
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        // Ejecutar el método que deseas probar
        HandleResponseTransaction response = cardTransaction.Purchase(cardId, 500.0);
        // Verificar que la respuesta no sea nula
        assertNotNull(response);
        // Verificar que la respuesta tenga un estado exitoso (código 200)
        assertEquals(200, response.getCode());

        // Verificar que la respuesta contenga una transacción
        assertNotNull(response.getTransaction());
        assertTrue(response.getTransaction() instanceof Transaction);
        // Verificar que el saldo de la tarjeta se haya actualizado correctamente
        assertEquals(500.0, card.getBalance());
    }

    @Test
    void testGetTransaction() {
        // Configurar comportamiento simulado del repositorio de transacciones
        Long transactionId = 123L;
        Transaction transaction = new Transaction();
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        // Ejecutar el método que deseas probar
        HandleResponseTransaction response = cardTransaction.GetTransaction(transactionId);

        // Verificar que la respuesta no sea nula
        assertNotNull(response);

        // Verificar que la respuesta tenga un estado exitoso (código 200)
        assertEquals(200, response.getCode());

        // Verificar que la respuesta contenga una transacción
        assertNotNull(response.getTransaction());
        assertTrue(response.getTransaction() instanceof Transaction);
    }

    @Test
    void testCancelTransaction() {
        // Configurar comportamiento simulado del repositorio de transacciones
        Long transactionId = 123L;
        Transaction transaction = new Transaction();
        LocalDateTime transactionDate = LocalDateTime.now(); //
        transaction.setTransactionDate(transactionDate);
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        // Configurar comportamiento simulado del repositorio de tarjetas
        String cardId = "1234567890123456";
        Card card = new Card();
        card.setCardId(cardId);
        card.setBalance(500.0);
        when(cardRepository.findByCardId(cardId)).thenReturn(card);
        // Ejecutar el método que deseas probar
        HandleResponseTransaction response = cardTransaction.CancelTransaction(cardId, transactionId);
        // Verificar que la respuesta no sea nula
        assertNotNull(response);

        // Verificar que la respuesta tenga un estado exitoso (código 200)
        assertEquals(200, response.getCode());

        // Verificar que la transacción esté marcada como cancelada
        assertTrue(transaction.isCanceled());

        // Verificar que el saldo de la tarjeta se haya actualizado correctamente
        assertEquals(500.0, card.getBalance());
    }



}
