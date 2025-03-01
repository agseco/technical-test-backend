package com.playtomic.tests.wallet.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(expectedWallet));

        // When
        Optional<Wallet> result = fetchWallet.fetch(walletId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedWallet);
        verify(walletRepository, times(1)).findById(walletId);
    }

    @Test
    void shouldReturnEmptyWhenWalletNotFound() {
        // Given
        Wallet.Id walletId = WalletFaker.Id.random();

        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        // When
        Optional<Wallet> result = fetchWallet.fetch(walletId);

        // Then
        assertThat(result).isEmpty();
        verify(walletRepository, times(1)).findById(walletId);
    }
}

