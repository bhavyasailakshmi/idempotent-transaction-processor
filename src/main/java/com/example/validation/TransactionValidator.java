
@Component
public class TransactionValidator {

    public void validate(TransactionRequest request) {

        if (request.getTransactionId() == null)
            throw new ValidationException("Missing transactionId");

        if (request.getRequestId() == null)
            throw new ValidationException("Missing requestId");

        if (request.getAccountId() == null)
            throw new ValidationException("Missing accountId");

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException("Amount must be positive");
    }
}
