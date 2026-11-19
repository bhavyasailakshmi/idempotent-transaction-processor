
@Service
public class SequenceHandlerService {

    private final Map<String,Integer> accountSequenceMap =
            new ConcurrentHashMap<>();

    public boolean canProcess(TransactionRequest txn) {

        int expectedSequence =
                accountSequenceMap.getOrDefault(
                        txn.getAccountId(),
                        1);

        return txn.getSequenceNumber() == expectedSequence;
    }

    public void markProcessed(TransactionRequest txn) {

        accountSequenceMap.put(
                txn.getAccountId(),
                txn.getSequenceNumber() + 1
        );
    }
}
