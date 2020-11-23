package model;

import org.immutables.value.Value;

import java.util.Map;

@Value.Immutable
public interface Report {

    Map<String, Long> topRepeatedIps();

    Map<String, Long> topRepeatedUris();

    int uniqueIpCount();
}
