
@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final ProcessedTransactionStore store;

    public boolean isDuplicate(String transactionId) {

        return store.alreadyProcessed(transactionId);
    }

    public void markProcessed(String transactionId) {

        store.markProcessed(transactionId);
    }
}
