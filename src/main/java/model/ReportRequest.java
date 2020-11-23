package model;

import org.immutables.value.Value;

import java.net.URL;

@Value.Immutable
public interface ReportRequest {
    URL getLogFile();

    int getTopRepeatedIpCount();

    int getTopRepeatedUriCount();

    boolean uriIncludeHost();
}
