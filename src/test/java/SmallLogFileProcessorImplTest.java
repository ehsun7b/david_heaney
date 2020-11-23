import model.ImmutableReportRequest;
import model.Report;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SmallLogFileProcessorImplTest {

    private SmallLogFileProcessorImpl logFileProcessor;

    @BeforeEach
    void before() {
        logFileProcessor = new SmallLogFileProcessorImpl();
    }

    @Test
    void process_success_sampleFile() throws IOException {
        final URL url = getClass().getClassLoader().getResource("programming-task-example-data.log");
        final Report report = logFileProcessor.process(
                ImmutableReportRequest
                        .builder()
                        .topRepeatedIpCount(3)
                        .topRepeatedUriCount(3)
                        .uriIncludeHost(false)
                        .logFile(url)
                        .build()
        );
        assertThat(report.uniqueIpCount()).isEqualTo(11);
        assertThat(report.topRepeatedIps()).isEqualTo(Map.of(
                "168.41.191.40", 4L,
                "50.112.00.11", 3L,
                "177.71.128.21", 3L
        ));
        assertThat(report.topRepeatedUris()).isEqualTo(Map.of(
                "/docs/manage-websites/", 2L,
                "/blog/2018/08/survey-your-opinion-matters/", 1L,
                "/newsletter/", 1L
        ));
    }

    @Test
    void process_success_sampleFile_uriIncludeHost() throws IOException {
        final URL url = getClass().getClassLoader().getResource("data_1.log");
        final Report report = logFileProcessor.process(
                ImmutableReportRequest
                        .builder()
                        .topRepeatedIpCount(3)
                        .topRepeatedUriCount(3)
                        .uriIncludeHost(true)
                        .logFile(url)
                        .build()
        );
        assertThat(report.uniqueIpCount()).isEqualTo(11);
        assertThat(report.topRepeatedIps()).isEqualTo(Map.of(
                "168.41.191.40", 10L,
                "50.112.00.11", 3L,
                "177.71.128.21", 3L
        ));
        assertThat(report.topRepeatedUris()).isEqualTo(Map.of(
                "http://example.net/blog/category/meta/", 5L,
                "168.41.191.40/docs/manage-websites/", 3L,
                "50.112.00.28/faq/how-to-install/", 2L
        ));
    }
}
