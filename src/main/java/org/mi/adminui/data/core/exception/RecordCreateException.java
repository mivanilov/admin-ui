package org.mi.adminui.data.core.exception;

public class RecordCreateException extends RuntimeException {

    public RecordCreateException(String message) {
        super(message);
    }

    public RecordCreateException(Throwable cause) {
        super(cause);
    }

    public RecordCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
