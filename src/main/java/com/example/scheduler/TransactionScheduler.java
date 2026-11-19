package com.example.transactionprocessor.scheduler;

import com.example.transactionprocessor.ingestion.JsonTransactionLoader;
import com.example.transactionprocessor.model.TransactionRequest;
import com.example.transactionprocessor.reporting.ProcessingReportService;
import com.example.transactionprocessor.service.TransactionProcessorService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionScheduler {

    private final JsonTransactionLoader loader;

    private final TransactionProcessorService processor;

    private final ProcessingReportService reportService;

    public TransactionScheduler(
            JsonTransactionLoader loader,
            TransactionProcessorService processor,
            ProcessingReportService reportService) {

        this.loader = loader;
        this.processor = processor;
        this.reportService = reportService;
    }

    @PostConstruct
    public void processTransactions() throws Exception {

        List<TransactionRequest> transactions =
                loader.loadTransactions();

        for (TransactionRequest request : transactions) {

            processor.process(request);
        }

        reportService.printSummary();
    }
}
