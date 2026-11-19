package com.example.transactionprocessor.service;

import com.example.transactionprocessor.exception.RetryableException;
import com.example.transactionprocessor.model.*;
import com.example.transactionprocessor.repository.AccountBalanceStore;
import com.example.transactionprocessor.repository.TransactionStore;
import com.example.transactionprocessor.validation.TransactionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TransactionProcessorService {

    private static final Logger log =
            LoggerFactory.getLogger(
                    TransactionProcessorService.class);

    private final TransactionValidator validator;
    private final TransactionStore transactionStore;
    private final IdempotencyService idempotencyService;
    private final RetryService retryService;
    private final SequenceHandlerService sequenceService;
    private final AccountBalanceStore balanceStore;

    public TransactionProcessorService(
            TransactionValidator validator,
            TransactionStore transactionStore,
            IdempotencyService idempotencyService,
            RetryService retryService,
            SequenceHandlerService sequenceService,
            AccountBalanceStore balanceStore) {

        this.validator = validator;
        this.transactionStore = transactionStore;
        this.idempotencyService = idempotencyService;
        this.retryService = retryService;
        this.sequenceService = sequenceService;
        this.balanceStore = balanceStore;
    }

    public ProcessingResult process(
            TransactionRequest request) {

        try {

            validator.validate(request);

            if (idempotencyService
                    .isDuplicate(
                            request.getTransactionId())) {

                log.warn("Duplicate detected {}",
                        request.getTransactionId());

                transactionStore.updateStatus(
                        request.getTransactionId(),
                        TransactionStatus.DUPLICATE);

                return new ProcessingResult(
                        request.getTransactionId(),
                        TransactionStatus.DUPLICATE,
                        "Duplicate Transaction");
            }

            if (!sequenceService.canProcess(request)) {

                transactionStore.updateStatus(
                        request.getTransactionId(),
                        TransactionStatus.PENDING);

                return new ProcessingResult(
                        request.getTransactionId(),
                        TransactionStatus.PENDING,
                        "Out of Order Transaction");
            }

            simulateFailure(request);

            applyBusinessEffect(request);

            idempotencyService.markProcessed(
                    request.getTransactionId());

            sequenceService.markProcessed(request);

            transactionStore.updateStatus(
                    request.getTransactionId(),
                    TransactionStatus.PROCESSED);

            return new ProcessingResult(
                    request.getTransactionId(),
                    TransactionStatus.PROCESSED,
                    "Success");

        } catch (RetryableException ex) {

            return handleRetry(request);

        } catch (Exception ex) {

            transactionStore.updateStatus(
                    request.getTransactionId(),
                    TransactionStatus.FAILED);

            return new ProcessingResult(
                    request.getTransactionId(),
                    TransactionStatus.FAILED,
                    ex.getMessage());
        }
    }

    private void applyBusinessEffect(
            TransactionRequest request) {

        switch (request.getTransactionType()) {

            case CREDIT ->
                    balanceStore.credit(
                            request.getAccountId(),
                            request.getAmount());

            case DEBIT ->
                    balanceStore.debit(
                            request.getAccountId(),
                            request.getAmount());

            default -> {
            }
        }
    }

    private void simulateFailure(
            TransactionRequest request) {

        if ("TXN-2005".equals(
                request.getTransactionId())) {

            throw new RetryableException(
                    "Transient Service Error");
        }
    }

    private ProcessingResult handleRetry(
            TransactionRequest request) {

        if (retryService.canRetry(
                request.getTransactionId())) {

            retryService.increment(
                    request.getTransactionId());

            transactionStore.updateStatus(
                    request.getTransactionId(),
                    TransactionStatus.RETRY_PENDING);

            return new ProcessingResult(
                    request.getTransactionId(),
                    TransactionStatus.RETRY_PENDING,
                    "Retry Scheduled");
        }

        transactionStore.updateStatus(
                request.getTransactionId(),
                TransactionStatus.FAILED);

        return new ProcessingResult(
                request.getTransactionId(),
                TransactionStatus.FAILED,
                "Retry Exhausted");
    }
}
