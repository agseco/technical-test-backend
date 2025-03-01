package com.playtomic.tests.wallet.infrastructure.jpa;

import com.playtomic.tests.wallet.domain.UserId;
import com.playtomic.tests.wallet.domain.Wallet;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@ToString
@Entity
@Table(name = "wallets")
public class WalletEntity {

    @Id
    private UUID id;
    private UUID userId;
    private BigDecimal balance;

    @Version
    private Long version;

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
