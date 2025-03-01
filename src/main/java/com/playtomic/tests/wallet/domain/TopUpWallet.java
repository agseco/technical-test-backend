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
        paymentGateway.charge(command.cardDetails(), command.amount);

        Wallet wallet = repository.findById(command.walletId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Wallet %s not found", command.walletId)));

        return repository.save(wallet.topUp(command.amount));
    }

    public record TopUpCommand(
            Wallet.Id walletId,
            PaymentGateway.CardDetails cardDetails,
            BigDecimal amount
    ) { }
}
