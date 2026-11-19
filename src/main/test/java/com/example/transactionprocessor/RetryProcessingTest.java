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
class RetryProcessingTest {

    @Autowired
    private TransactionProcessorService processor;

    @Test
    void shouldSendTransactionForRetry() {

        TransactionRequest request =
                new TransactionRequest(
                        "TXN-2005",
                        "REQ-2005",
                        "ACC-200",
                        TransactionType.CREDIT,
                        BigDecimal.valueOf(500),
                        "USD",
                        1);

        var result =
                processor.process(request);

        assertEquals(
                TransactionStatus.RETRY_PENDING,
                result.getStatus());
    }
}
