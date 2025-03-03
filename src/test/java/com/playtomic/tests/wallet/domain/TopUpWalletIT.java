package com.playtomic.tests.wallet.domain;

import com.playtomic.tests.util.Constants;
import com.playtomic.tests.wallet.infrastructure.jdbc.JdbcWalletRepository;
import com.playtomic.tests.wallet.infrastructure.jdbc.JdbcWalletRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TopUpWalletIT {

    @Autowired
    private JdbcWalletRepository walletRepository;

    @Autowired
    private JdbcWalletRepositoryAdapter walletRepositoryAdapter;

    @MockitoBean
    private PaymentGateway paymentGateway;

    @Autowired
    private TopUpWallet topUpWallet;

    private static final Wallet WALLET = WalletFaker.withBalance(100.0);

    @BeforeEach
    void setUp() {
        walletRepository.deleteAll();
        walletRepositoryAdapter.insert(WALLET);
    }

    @Test
    void topUpShouldNotModifyBalanceWhenChargeFails() throws Exception {
        PaymentGateway.CardDetails cardDetails = cardDetails();
        BigDecimal amountToTopUp = BigDecimal.valueOf(50.0);
        TopUpWallet.TopUpCommand command = new TopUpWallet.TopUpCommand(WALLET.getId(), cardDetails, amountToTopUp);

        doThrow(new RuntimeException("Charge failed"))
                .when(paymentGateway)
                .charge(any(PaymentGateway.CardDetails.class), any(BigDecimal.class));

        assertThrows(RuntimeException.class, () -> topUpWallet.topUp(command));

        Optional<Wallet> wallet = walletRepositoryAdapter.findById(WALLET.getId());
        assertThat(wallet).isPresent()
                .hasValueSatisfying(w -> {
                    assertThat(w.getBalance())
                            .usingComparator(BigDecimal::compareTo)
                            .isEqualTo(WALLET.getBalance());
                });
    }

    private PaymentGateway.CardDetails cardDetails() {
        return new PaymentGateway.CardDetails(Constants.CARD_NUMBER);
    }
}
