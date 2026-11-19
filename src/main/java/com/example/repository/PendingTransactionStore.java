package com.example.transactionprocessor.repository;

import com.example.transactionprocessor.model.TransactionRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class PendingTransactionStore {

    private final List<TransactionRequest> pendingTransactions =
            new CopyOnWriteArrayList<>();

    public void add(TransactionRequest request) {

        pendingTransactions.add(request);
    }

    public List<TransactionRequest> getPendingTransactions() {

        return pendingTransactions;
    }

    public void remove(TransactionRequest request) {

        pendingTransactions.remove(request);
    }
}
