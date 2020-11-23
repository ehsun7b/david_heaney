import model.Record;

public abstract class AbstractLogFileProcessor implements LogFileProcessor {

    protected String resolveUri(Record record, boolean uriIncludeHost) {
        if (uriIncludeHost && record.getUri().startsWith("/")) {
            return record.getIp().concat(record.getUri());
        }

        return record.getUri();
    }
}
