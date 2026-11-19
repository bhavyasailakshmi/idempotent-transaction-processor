
@Builder
@Data
public class ProcessingResult {

    private String transactionId;

    private TransactionStatus status;

    private String message;
}
