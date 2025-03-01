package com.playtomic.tests.wallet.application;

import com.playtomic.tests.core.domain.EntityNotFoundException;
import com.playtomic.tests.wallet.domain.FetchWallet;
import com.playtomic.tests.wallet.domain.PaymentGateway;
import com.playtomic.tests.wallet.domain.TopUpWallet;
import com.playtomic.tests.wallet.domain.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    private final Logger log = LoggerFactory.getLogger(WalletController.class);

    private final FetchWallet fetchWallet;
    private final TopUpWallet topUpWallet;

    @Autowired
    public WalletController(FetchWallet fetchWallet, TopUpWallet topUpWallet) {
        this.fetchWallet = fetchWallet;
        this.topUpWallet = topUpWallet;
    }

    @GetMapping("/{id}")
    WalletResponse get(@PathVariable String id) {
        Wallet wallet = fetchWallet.fetch(Wallet.Id.of(id))
                .orElseThrow(() -> new EntityNotFoundException(id));
        return WalletResponse.from(wallet);
    }

    @PutMapping("/{id}")
    WalletResponse topUp(@RequestBody TopUpRequest request, @PathVariable String id) {
        Wallet toppedUpWallet = topUpWallet.topUp(
                new TopUpWallet.TopUpCommand(
                        Wallet.Id.of(id),
                        new PaymentGateway.CardDetails(request.cardNumber),
                        request.amount
                )
        );
        return WalletResponse.from(toppedUpWallet);
    }

    public record WalletResponse(
            String id,
            String userId,
            BigDecimal balance
    ) {
        public static WalletResponse from(Wallet wallet) {
            return new WalletResponse(
                wallet.getId().id().toString(),
                wallet.getUserId().id().toString(),
                wallet.getBalance()
            );
        }
    }

    public record TopUpRequest(
        String cardNumber,
        BigDecimal amount
    ) { }
}
