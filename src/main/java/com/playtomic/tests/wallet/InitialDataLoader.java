package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.domain.UserId;
import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class InitialDataLoader implements CommandLineRunner {

    private final WalletRepository repository;

    @Autowired
    public InitialDataLoader(WalletRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        repository.save(new Wallet(
                Wallet.Id.of("b4efa483-376d-4ef5-9383-cf16cb49f28d"),
                UserId.of("ff2ee5bd-b419-40cd-a603-5f6a6174c246"),
                BigDecimal.valueOf(100)
        ));
    }
}