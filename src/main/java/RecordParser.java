import model.ImmutableRecord;
import model.Record;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecordParser {
    final private String regex = "^(\\S+) (\\S+) (\\S+) " +
            "\\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+)" +
            " (\\S+)\\s*(\\S+)?\\s*\" (\\d{3}) (\\S+)";
    final private Pattern pattern = Pattern.compile(regex);

    public Record parse(String record) {
        final Matcher matcher = pattern.matcher(record);

        if (matcher.find()) {
            final String ip = matcher.group(1);
            final String responseStr = matcher.group(8);
            final int response = Integer.parseInt(responseStr);
            final String uri = matcher.group(6);
            return ImmutableRecord
                    .builder()
                    .ip(ip)
                    .uri(uri)
                    .responseCode(response)
                    .build();

        } else {
            throw new IllegalArgumentException(String.format("bad log record, %s", record));
        }
    }
}
