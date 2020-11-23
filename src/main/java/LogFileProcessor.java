import model.Report;
import model.ReportRequest;

import java.io.IOException;

public interface LogFileProcessor {

    Report process(ReportRequest request) throws IOException;
}
