@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    private String transactionId;

    private String requestId;

    private String accountId;

    private TransactionType transactionType;

    private BigDecimal amount;

    private String currency;

    private Integer sequenceNumber;
}
