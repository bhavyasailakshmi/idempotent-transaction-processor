package com.example.transactionprocessor.repository;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccountBalanceStore {

    private final Map<String, BigDecimal> balances =
            new ConcurrentHashMap<>();

    public BigDecimal getBalance(String accountId) {

        return balances.getOrDefault(
                accountId,
                BigDecimal.ZERO);
    }

    public void credit(
            String accountId,
            BigDecimal amount) {

        balances.merge(
                accountId,
                amount,
                BigDecimal::add);
    }

    public void debit(
            String accountId,
            BigDecimal amount) {

        balances.merge(
                accountId,
                amount.negate(),
                BigDecimal::add);
    }
}
