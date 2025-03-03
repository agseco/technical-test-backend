package com.playtomic.tests.wallet.domain;

import com.playtomic.tests.core.domain.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Wallet topUp(TopUpCommand command) {
        Wallet wallet = repository.findByIdWithPessimisticLocking(command.walletId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Wallet %s not found", command.walletId)));

        Wallet toppedUpWallet = repository.save(wallet.topUp(command.amount));
        paymentGateway.charge(command.cardDetails(), command.amount);

        return toppedUpWallet;
    }

    public record TopUpCommand(
            Wallet.Id walletId,
            PaymentGateway.CardDetails cardDetails,
            BigDecimal amount
    ) { }
}
