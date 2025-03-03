CREATE TABLE wallets (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    balance DECIMAL(19,2) NOT NULL
);
