package com.playtomic.tests.wallet.infrastructure.jpa;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.WalletFaker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(JpaWalletRepositoryAdapter.class)
class JpaWalletRepositoryAdapterIT {

    @Autowired
    private JpaWalletRepositoryAdapter walletRepositoryAdapter;

    @Test
    void shouldSaveAndRetrieveWallet() {
        // Given
        Wallet wallet = WalletFaker.random();

        // When
        Wallet savedWallet = walletRepositoryAdapter.save(wallet);

        // Then
        assertThat(savedWallet).isNotNull();
        assertThat(savedWallet.getId()).isEqualTo(wallet.getId());
        assertThat(savedWallet.getUserId()).isEqualTo(wallet.getUserId());
        assertThat(savedWallet.getBalance()).isEqualTo(wallet.getBalance());
    }

    @Test
    void shouldRetrieveExistingWalletById() {
        // Given
        Wallet wallet = WalletFaker.random();
        walletRepositoryAdapter.save(wallet);

        // When
        Optional<Wallet> retrievedWallet = walletRepositoryAdapter.findById(wallet.getId());

        // Then
        assertThat(retrievedWallet).isEqualTo(Optional.of(wallet));
    }

    @Test
    void shouldReturnEmptyWhenWalletDoesNotExsit() {
        // Given
        Wallet wallet = WalletFaker.random();

        // When
        Optional<Wallet> retrievedWallet = walletRepositoryAdapter.findById(wallet.getId());

        // Then
        assertThat(retrievedWallet).isNotPresent();
    }
}

