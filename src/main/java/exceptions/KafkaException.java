package exceptions;

import java.nio.ByteBuffer;

public class KafkaException extends RuntimeException {
    private byte[] errorCode;

    public KafkaException(short error) {
        super();
        errorCode = ByteBuffer.allocate(2)
            .putShort(error)
            .array();
    }

    public byte[] errorCode() {
        return errorCode;
    }
}
