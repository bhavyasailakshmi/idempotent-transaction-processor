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
class DuplicateTransactionTest {

    @Autowired
    TransactionProcessorService processor;

    @Test
    void shouldDetectDuplicateTransaction() {

        TransactionRequest request =
                new TransactionRequest(
                        "TXN-100",
                        "REQ-100",
                        "ACC-100",
                        TransactionType.CREDIT,
                        BigDecimal.valueOf(100),
                        "USD",
                        1);

        processor.process(request);

        var response =
                processor.process(request);

        assertEquals(
                TransactionStatus.DUPLICATE,
                response.getStatus());
    }
}
