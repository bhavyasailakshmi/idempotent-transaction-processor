@Service
@RequiredArgsConstructor
public class ProcessingReportService {

    private final TransactionStore store;

    public void printSummary() {

        Map<TransactionStatus, Long> report =
                store.getAll()
                        .values()
                        .stream()
                        .collect(Collectors.groupingBy(
                                Function.identity(),
                                Collectors.counting()));

        report.forEach(
          (status,count) ->
              System.out.println(
                   status + " : " + count)
        );
    }
}
