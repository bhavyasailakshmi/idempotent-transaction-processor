
@Repository
public class ProcessedTransactionStore {

    private final Set<String> processedTransactions =
            ConcurrentHashMap.newKeySet();

    public boolean alreadyProcessed(String transactionId) {

        return processedTransactions.contains(transactionId);
    }

    public void markProcessed(String transactionId) {

        processedTransactions.add(transactionId);
    }
}
