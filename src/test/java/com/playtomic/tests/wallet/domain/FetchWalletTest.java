package com.playtomic.tests.wallet.domain;

import com.playtomic.tests.core.domain.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FetchWalletTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private FetchWallet fetchWallet;

    @Test
    void shouldReturnWalletWhenPresent() {
        // Given
        Wallet.Id walletId = WalletFaker.Id.random();
        Wallet expectedWallet = WalletFaker.withId(walletId);

        when(walletRepository.findById(walletId))
                .thenReturn(Optional.of(expectedWallet));

        // When
        Wallet result = fetchWallet.fetch(walletId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedWallet);
        verify(walletRepository, times(1)).findById(walletId);
    }

    @Test
    void shouldThrowOnWalletNotFound() {
        // Given
        Wallet.Id walletId = WalletFaker.Id.random();

        when(walletRepository.findById(walletId))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class, () -> {
            fetchWallet.fetch(walletId);
        });
        verify(walletRepository, times(1))
                .findById(walletId);
    }
}

