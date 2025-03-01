package com.playtomic.tests.wallet.infrastructure.jpa;

import com.playtomic.tests.wallet.domain.UserId;
import com.playtomic.tests.wallet.domain.Wallet;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.util.UUID;

@Entity
@Table(name = "wallets")
public class WalletEntity {

    @Id
    private UUID id;
    private UUID userId;
    private double balance;

    @Version
    private Long version;

    protected WalletEntity() {}

    public WalletEntity(UUID id, UUID userId, double balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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

    @Override
    public String toString() {
        return "WalletEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", balance=" + balance +
                ", version=" + version +
                '}';
    }
}
