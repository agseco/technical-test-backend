package com.playtomic.tests.wallet.infrastructure.jdbc;

import com.playtomic.tests.wallet.domain.UserId;
import com.playtomic.tests.wallet.domain.Wallet;
import lombok.Getter;
import lombok.NonNull;
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
    @NonNull private UUID id;
    @NonNull private UUID userId;
    @NonNull private BigDecimal balance;

    protected WalletEntity() { }

    public WalletEntity(@NonNull UUID id, @NonNull UUID userId, @NonNull BigDecimal balance) {
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
