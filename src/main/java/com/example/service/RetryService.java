@Service
@RequiredArgsConstructor
public class RetryService {

    private static final int MAX_RETRY = 3;

    private final RetryStore retryStore;

    public boolean canRetry(String txnId){

        return retryStore.getRetryCount(txnId)
                < MAX_RETRY;
    }

    public void incrementRetry(String txnId){

        retryStore.incrementRetry(txnId);
    }
}
