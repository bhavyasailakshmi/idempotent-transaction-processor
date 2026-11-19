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
class OutOfOrderTransactionTest {

    @Autowired
    private TransactionProcessorService processor;

    @Test
    void shouldHandleOutOfOrderTransaction() {

        TransactionRequest txn2 =
                new TransactionRequest(
                        "TXN-2",
                        "REQ-2",
                        "ACC-500",
                        TransactionType.DEBIT,
                        BigDecimal.valueOf(50),
                        "USD",
                        2);

        var result =
                processor.process(txn2);

        assertEquals(
                TransactionStatus.PENDING,
                result.getStatus());
    }
}
