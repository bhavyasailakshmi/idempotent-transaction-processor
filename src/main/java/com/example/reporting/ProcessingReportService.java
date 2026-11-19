package com.example.transactionprocessor.reporting;

import com.example.transactionprocessor.model.TransactionStatus;
import com.example.transactionprocessor.repository.TransactionStore;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProcessingReportService {

    private final TransactionStore transactionStore;

    public ProcessingReportService(
            TransactionStore transactionStore) {

        this.transactionStore = transactionStore;
    }

    public void printSummary() {

        Map<TransactionStatus, Long> summary =
                transactionStore.getAllStatuses()
                        .values()
                        .stream()
                        .collect(Collectors.groupingBy(
                                Function.identity(),
                                Collectors.counting()));

        System.out.println(
                "================================");

        System.out.println(
                "PROCESSING SUMMARY");

        System.out.println(
                "================================");

        summary.forEach((status, count) ->
                System.out.println(
                        status + " : " + count));
    }
}
