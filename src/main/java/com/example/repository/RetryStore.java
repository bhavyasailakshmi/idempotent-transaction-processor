@Repository
public class RetryStore {

    private final Map<String, Integer> retryMap =
            new ConcurrentHashMap<>();

    public int getRetryCount(String transactionId) {

        return retryMap.getOrDefault(transactionId, 0);
    }

    public void incrementRetry(String transactionId) {

        retryMap.merge(transactionId,
                1,
                Integer::sum);
    }
}
