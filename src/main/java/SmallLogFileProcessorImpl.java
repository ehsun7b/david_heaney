import model.ImmutableReport;
import model.Record;
import model.Report;
import model.ReportRequest;
import util.MapUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;


/**
 * This interface is targeting log files that are small enough to be loaded in the memory,
 * for larger files use LargeLogFileProcessorImpl
 */
public class SmallLogFileProcessorImpl extends AbstractLogFileProcessor {
    final static Logger log = Logger.getLogger(SmallLogFileProcessorImpl.class.getName());

    final RecordParser parser = new RecordParser();

    @Override
    public Report process(ReportRequest request) throws IOException {
        try (final InputStream inputStream = request.getLogFile().openStream()) {
            final List<Record> recordList = loadAllRecords(inputStream);

            final Map<String, Long> mapIpFrequency = recordList
                    .stream()
                    .collect(groupingBy(Record::getIp, counting()));

            final Map<String, Long> mapIpFrequencySorted = MapUtil.sortByValue(mapIpFrequency, (a, b) -> Long.compare(b, a));

            final Map<String, Long> mapUriFrequency = recordList
                    .stream()
                    .collect(groupingBy(record -> this.resolveUri(record, request.uriIncludeHost()), counting()));

            final Map<String, Long> mapUriFrequencySorted = MapUtil.sortByValue(mapUriFrequency, (a, b) -> Long.compare(b, a));

            return ImmutableReport
                    .builder()
                    .uniqueIpCount(mapIpFrequencySorted.keySet().size())
                    .topRepeatedIps(MapUtil.topOfMap(mapIpFrequencySorted, request.getTopRepeatedIpCount()))
                    .topRepeatedUris(MapUtil.topOfMap(mapUriFrequencySorted, request.getTopRepeatedUriCount()))
                    .build();
        }
    }

    private List<Record> loadAllRecords(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()))
                .lines()
                .map(line -> {
                    try {
                        return parser.parse(line);
                    } catch (IllegalArgumentException ex) {
                        log.log(WARNING, ex.getMessage());
                        return Record.EMPTY;
                    } catch (Exception ex) {
                        log.log(SEVERE, ex, () -> String.format("Exception while parsing line'%s'", line));
                        return Record.EMPTY;
                    }
                })
                .filter(record -> !record.equals(Record.EMPTY))
                .collect(Collectors.toList());
    }
}
