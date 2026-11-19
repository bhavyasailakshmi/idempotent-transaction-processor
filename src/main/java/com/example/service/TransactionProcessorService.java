
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionProcessorService {

    private final TransactionValidator validator;
    private final TransactionStore store;
    private final IdempotencyService idempotencyService;
    private final RetryService retryService;
    private final SequenceHandlerService sequenceService;

    public ProcessingResult process(
            TransactionRequest request) {

        try {

            validator.validate(request);

            if(idempotencyService
                    .isDuplicate(request.getTransactionId())) {

                store.updateStatus(
                        request.getTransactionId(),
                        TransactionStatus.DUPLICATE);

                return result(
                        request,
                        TransactionStatus.DUPLICATE,
                        "Duplicate");
            }

            if(!sequenceService.canProcess(request)) {

                store.updateStatus(
                        request.getTransactionId(),
                        TransactionStatus.PENDING);

                return result(
                        request,
                        TransactionStatus.PENDING,
                        "Out of order");
            }

            simulateFailure(request);

            sequenceService.markProcessed(request);

            idempotencyService.markProcessed(
                    request.getTransactionId());

            store.updateStatus(
                    request.getTransactionId(),
                    TransactionStatus.PROCESSED);

            return result(
                    request,
                    TransactionStatus.PROCESSED,
                    "Success");

        }
        catch (RetryableException ex) {

            return handleRetry(request);
        }
        catch (Exception ex) {

            store.updateStatus(
                    request.getTransactionId(),
                    TransactionStatus.FAILED);

            return result(
                    request,
                    TransactionStatus.FAILED,
                    ex.getMessage());
        }
    }
}
