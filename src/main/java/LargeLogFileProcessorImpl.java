import model.ImmutableReport;
import model.Record;
import model.Report;
import model.ReportRequest;
import util.MapUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;

/**
 * For the sake of efficiency this class keeps state!
 * Do not use this implementation unless the log file is too big for SmallLogFileProcessorImpl
 */
public class LargeLogFileProcessorImpl extends AbstractLogFileProcessor {
    final static Logger log = Logger.getLogger(LargeLogFileProcessorImpl.class.getName());

    final Map<String, Long> ipFrequency = new LinkedHashMap<>();
    final Map<String, Long> uriFrequency = new LinkedHashMap<>();

    @Override
    public Report process(ReportRequest request) throws IOException {
        final RecordParser parser = new RecordParser();

        try (final InputStream inputStream = request.getLogFile().openStream();
             final Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {

            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();

                try {
                    final Record record = parser.parse(line);
                    process(record, request);
                } catch (IllegalArgumentException ex) {
                    log.log(WARNING, ex.getMessage());
                }
            }
        }

        final Map<String, Long> ipFreqSorted = MapUtil.sortByValue(ipFrequency, (a, b) -> Long.compare(b, a));
        final Map<String, Long> uriFreqSorted = MapUtil.sortByValue(uriFrequency, (a, b) -> Long.compare(b, a));

        return ImmutableReport
                .builder()
                .uniqueIpCount(ipFrequency.keySet().size())
                .topRepeatedUris(MapUtil.topOfMap(uriFreqSorted, request.getTopRepeatedUriCount()))
                .topRepeatedIps(MapUtil.topOfMap(ipFreqSorted, request.getTopRepeatedIpCount()))
                .build();
    }

    private void process(Record record, ReportRequest request) {
        processIp(record.getIp());
        processUri(this.resolveUri(record, request.uriIncludeHost()));
    }

    private void processUri(String uri) {
        processFrequency(uriFrequency, uri);
    }


    private void processIp(String ip) {
        processFrequency(ipFrequency, ip);
    }

    private void processFrequency(Map<String, Long> map, String key) {
        Long freq = map.get(key);

        if (freq != null) {
            freq++;
        } else {
            freq = 1L;
        }

        map.put(key, freq);
    }
}
