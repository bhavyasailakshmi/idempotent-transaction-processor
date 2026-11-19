package com.example.transactionprocessor;

import com.example.transactionprocessor.model.TransactionRequest;
import com.example.transactionprocessor.model.TransactionStatus;
import com.example.transactionprocessor.model.TransactionType;
import com.example.transactionprocessor.service.TransactionProcessorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ValidationTest {

    @Autowired
    TransactionProcessorService processor;

    @Test
    void shouldRejectInvalidAmount() {

        TransactionRequest request =
                new TransactionRequest(
                        "TXN-101",
                        "REQ-101",
                        "ACC-101",
                        TransactionType.DEBIT,
                        BigDecimal.valueOf(-10),
                        "USD",
                        1);

        var result =
                processor.process(request);

        assertEquals(
                TransactionStatus.FAILED,
                result.getStatus());
    }
}
