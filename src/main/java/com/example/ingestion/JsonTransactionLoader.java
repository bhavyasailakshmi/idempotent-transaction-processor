package com.example.transactionprocessor.ingestion;

import com.example.transactionprocessor.model.TransactionRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JsonTransactionLoader {

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    public List<TransactionRequest> loadTransactions()
            throws IOException {

        return objectMapper.readValue(
                new ClassPathResource(
                        "transactions.json").getInputStream(),
                new TypeReference<List<TransactionRequest>>() {
                });
    }
}
