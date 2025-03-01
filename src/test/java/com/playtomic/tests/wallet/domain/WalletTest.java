package com.playtomic.tests.wallet.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WalletTest {

    @Test
    void shouldCreateWalletSuccessfully() {
        // Given
        UUID walletId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal initialBalance = BigDecimal.valueOf(100.0);

        // When
        Wallet wallet = new Wallet(Wallet.Id.of(walletId), new UserId(userId), initialBalance);

        // Then
        assertThat(wallet.getId()).isEqualTo(Wallet.Id.of(walletId));
        assertThat(wallet.getUserId()).isEqualTo(new UserId(userId));
        assertThat(wallet.getBalance()).isEqualTo(initialBalance);
    }

    @Test
    void shouldEnforceEqualityForSameId() {
        // Given
        Wallet.Id walletId = WalletFaker.Id.random();
        Wallet wallet1 = WalletFaker.withId(walletId);
        Wallet wallet2 = WalletFaker.withId(walletId);

        // Then
        assertThat(wallet1).isEqualTo(wallet2);
        assertThat(wallet1.hashCode()).isEqualTo(wallet2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentId() {
        // Given
        Wallet wallet1 = WalletFaker.random();
        Wallet wallet2 = WalletFaker.random();

        // Then
        assertThat(wallet1).isNotEqualTo(wallet2);
    }

    @Test
    void shouldTopUpBalanceSuccessfully() {
        // Given
        Wallet wallet = WalletFaker.withBalance(100.0);

        // When
        Wallet updatedWallet = wallet.topUp(BigDecimal.valueOf(50.0));

        // Then
        assertThat(updatedWallet.getBalance()).isEqualTo(BigDecimal.valueOf(150.0));
    }

    @Test
    void shouldThrowExceptionForNegativeTopUp() {
        // Given
        Wallet wallet = WalletFaker.withBalance(100.0);

        // Then
        assertThatThrownBy(() -> wallet.topUp(BigDecimal.valueOf(-10.0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Top up amount must be positive");
    }

    @Test
    void shouldThrowExceptionForZeroTopUp() {
        // Given
        Wallet wallet = WalletFaker.withBalance(100.0);

        // Then
        assertThatThrownBy(() -> wallet.topUp(BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Top up amount must be positive");
    }
}

