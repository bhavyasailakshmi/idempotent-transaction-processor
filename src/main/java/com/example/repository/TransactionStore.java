
@Repository
public class TransactionStore {

    private final Map<String, TransactionStatus> store =
            new ConcurrentHashMap<>();

    public void updateStatus(String txnId,
                             TransactionStatus status) {

        store.put(txnId, status);
    }

    public TransactionStatus getStatus(String txnId) {

        return store.get(txnId);
    }

    public Map<String, TransactionStatus> getAll() {

        return store;
    }
}
