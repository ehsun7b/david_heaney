package model;

import org.immutables.value.Value;

@Value.Immutable
public interface Record {

    Record EMPTY = ImmutableRecord.builder().ip("").responseCode(0).uri("").build();

    String getIp();

    String getUri();

    int getResponseCode();
}
