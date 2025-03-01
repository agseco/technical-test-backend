package com.playtomic.tests.wallet.infrastructure.jpa;

import com.playtomic.tests.core.ResourceNotFoundException;
import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.WalletFaker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(JpaWalletRepositoryAdapter.class) // Explicitly import the adapter
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
    void shouldRetrieveWalletById() {
        // Given
        Wallet wallet = WalletFaker.random();
        walletRepositoryAdapter.save(wallet);

        // When
        Wallet retrievedWallet = walletRepositoryAdapter.findById(wallet.getId());

        // Then
        assertThat(retrievedWallet).isEqualTo(wallet);
    }

    @Test
    void shouldThrowExceptionWhenWalletNotFound() {
        // Given
        Wallet.Id nonExistentId = WalletFaker.WalletIdFaker.random();

        // Then
        assertThatThrownBy(() -> walletRepositoryAdapter.findById(nonExistentId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

