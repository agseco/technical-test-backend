package com.playtomic.tests.wallet.domain;

import com.playtomic.tests.core.domain.EntityNotFoundException;
import com.playtomic.tests.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopUpWalletTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private PaymentGateway paymentGateway;

    @InjectMocks
    private TopUpWallet topUpWallet;

    @Test
    void shouldSuccessfullyTopUpAndCharge() {
        // Given
        Wallet wallet = WalletFaker.withBalance(100.0);
        PaymentGateway.CardDetails cardDetails = cardDetails();
        BigDecimal amountToTopUp = BigDecimal.valueOf(50.0);
        TopUpWallet.TopUpCommand command = new TopUpWallet.TopUpCommand(wallet.getId(), cardDetails, amountToTopUp);

        when(walletRepository.findByIdWithPessimisticLocking(wallet.getId()))
                .thenReturn(Optional.of(wallet));
        when(walletRepository.save(Mockito.any()))
                .thenReturn(new Wallet(wallet.getId(), wallet.getUserId(), wallet.getBalance().add(amountToTopUp)));  // The saved wallet has a topped-up balance

        // When
        Wallet toppedUpWallet = topUpWallet.topUp(command);

        // Then
        assertNotNull(toppedUpWallet);
        assertEquals(wallet.getId(), toppedUpWallet.getId());
        assertEquals(wallet.getBalance().add(amountToTopUp), toppedUpWallet.getBalance());

        verify(walletRepository, times(1))
                .findByIdWithPessimisticLocking(wallet.getId());
        verify(walletRepository, times(1))
                .save(Mockito.any());
        verify(paymentGateway, times(1))
                .charge(Mockito.any(), Mockito.any());
    }

    @Test
    void shouldThrowAndNotChargeOnWalletNotFound() {
        // Given
        Wallet wallet = WalletFaker.withBalance(100.0);
        PaymentGateway.CardDetails cardDetails = cardDetails();
        BigDecimal amountToTopUp = BigDecimal.valueOf(50.0);
        TopUpWallet.TopUpCommand command = new TopUpWallet.TopUpCommand(wallet.getId(), cardDetails, amountToTopUp);

        when(walletRepository.findByIdWithPessimisticLocking(wallet.getId()))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class, () -> {
            topUpWallet.topUp(command);
        });

        verify(walletRepository, times(1))
                .findByIdWithPessimisticLocking(wallet.getId());
        verify(walletRepository, times(0))
                .save(Mockito.any());
        verify(paymentGateway, times(0))
                .charge(Mockito.any(), Mockito.any());
    }

    private PaymentGateway.CardDetails cardDetails() {
        return new PaymentGateway.CardDetails(Constants.CARD_NUMBER);
    }
}


