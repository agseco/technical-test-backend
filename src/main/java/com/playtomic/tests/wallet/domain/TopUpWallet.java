package com.playtomic.tests.wallet.domain;

import com.playtomic.tests.core.domain.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TopUpWallet {
    private final WalletRepository repository;
    private final PaymentGateway paymentGateway;

    @Autowired
    public TopUpWallet(WalletRepository repository, PaymentGateway paymentGateway) {
        this.repository = repository;
        this.paymentGateway = paymentGateway;
    }

    @Transactional
    public Wallet topUp(@NonNull TopUpCommand command) {
        Wallet wallet = repository.findByIdAndLock(command.walletId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Wallet %s not found", command.walletId)));

        Wallet toppedUpWallet = wallet.topUp(command.amount);
        repository.update(toppedUpWallet);

        paymentGateway.charge(command.cardDetails(), command.amount);

        return toppedUpWallet;
    }

    public record TopUpCommand(
            @NonNull Wallet.Id walletId,
            @NonNull PaymentGateway.CardDetails cardDetails,
            @NonNull BigDecimal amount
    ) { }
}
