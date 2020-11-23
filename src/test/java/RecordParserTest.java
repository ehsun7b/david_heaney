import model.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecordParserTest {

    private RecordParser recordParser;

    @BeforeEach
    void before() {
        recordParser = new RecordParser();
    }

    @Test
    void parse_successWithSampleRecords() {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("programming-task-example-data.log");
        final Stream<String> lines = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset())).lines();
        lines.forEach(line -> recordParser.parse(line));
    }

    @Test
    void parse_success_verify_values() {
        final Record record1 = recordParser.parse("177.71.128.21 - - [10/Jul/2018:22:21:28 +0200] \"GET /intranet-analytics/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; fr-FR) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7\"");
        assertThat(record1.getIp()).isEqualTo("177.71.128.21");
        assertThat(record1.getResponseCode()).isEqualTo(200);
        assertThat(record1.getUri()).isEqualTo("/intranet-analytics/");

        final Record record2 = recordParser.parse("168.41.191.40 - - [09/Jul/2018:10:10:38 +0200] \"GET http://example.net/blog/category/meta/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_7) AppleWebKit/534.24 (KHTML, like Gecko) RockMelt/0.9.58.494 Chrome/11.0.696.71 Safari/534.24\"");
        assertThat(record2.getIp()).isEqualTo("168.41.191.40");
        assertThat(record2.getResponseCode()).isEqualTo(200);
        assertThat(record2.getUri()).isEqualTo("http://example.net/blog/category/meta/");
    }

    @Test
    void parse_fail_empty_line() {
        Throwable exceptionThatWasThrown = assertThrows(IllegalArgumentException.class, () -> recordParser.parse(""));

        assertThat(exceptionThatWasThrown.getMessage()).startsWith("bad log record,");
    }

    @Test
    void parse_fail_bad_line() {
        Throwable exceptionThatWasThrown = assertThrows(IllegalArgumentException.class, () -> recordParser.parse("this is a stupid log record"));

        assertThat(exceptionThatWasThrown.getMessage()).isEqualTo("bad log record, this is a stupid log record");
    }

    @Test
    void parse_fail_a_bit_malformed_record() {
        Throwable exceptionThatWasThrown = assertThrows(IllegalArgumentException.class, () -> recordParser.parse("0/Jul/2018:22:21:28 +0200] \"GET /intranet-analytics/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; fr-FR) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7\""));

        assertThat(exceptionThatWasThrown.getMessage()).startsWith("bad log record,");
    }
}
