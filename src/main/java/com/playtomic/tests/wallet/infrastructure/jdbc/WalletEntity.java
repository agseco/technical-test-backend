package com.playtomic.tests.wallet.infrastructure.jdbc;

import com.playtomic.tests.wallet.domain.UserId;
import com.playtomic.tests.wallet.domain.Wallet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@ToString
@Table(name = "wallets")
public class WalletEntity {

    @Id
    private UUID id;
    private UUID userId;
    private BigDecimal balance;

    protected WalletEntity() {}

    public WalletEntity(UUID id, UUID userId, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

    public Wallet toDomain() {
        return new Wallet(
                Wallet.Id.of(id),
                UserId.of(userId),
                balance
        );
    }

    public static WalletEntity fromDomain(Wallet wallet) {
        return new WalletEntity(
                wallet.getId().id(),
                wallet.getUserId().id(),
                wallet.getBalance()
        );
    }
}
