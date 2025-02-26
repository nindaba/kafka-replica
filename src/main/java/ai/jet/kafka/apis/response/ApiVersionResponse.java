package ai.jet.kafka.apis.response;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import ai.jet.kafka.apis.KafkaApi;
import ai.jet.kafka.apis.KafkaVerions;
import ai.jet.kafka.constants.ErrorCodes;

public class ApiVersionResponse implements ApiResponse {
    private int correlationId;
    private short errorCode;
    private byte apiKeysCount;
    private short apiKey;
    private short minVersion;
    private short maxVersion;
    private byte apiTagBuffer;
    private int throttledTime;
    private byte tagBuffer;


    public ApiVersionResponse(int correlationId) {
        this.correlationId = correlationId;
        errorCode = ErrorCodes.NONE;
        apiKeysCount = 2;
        apiKey = KafkaVerions.API_VERSIONS_KEY;
        apiTagBuffer = 0;
        throttledTime = 0;
        tagBuffer = 0;
    }

    public static ApiVersionResponse of(int correlationId) {
        return new ApiVersionResponse(correlationId);
    }

    public ApiVersionResponse errorCode(short errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public ApiVersionResponse minVersion(short minVersion) {
        this.minVersion = minVersion;
        return this;
    }

    public ApiVersionResponse maxVersion(short maxVersion) {
        this.maxVersion = maxVersion;
        return this;
    }


    @Override
    public byte[] getBytes() {
        var response = createBuffer();
        var size = response.remaining();
        var bytes = new byte[size + 4];

        response.get(bytes, 4, size);

        ByteBuffer.allocate(4)
            .putInt(size)
            .flip()
            .get(bytes, 0, 4);

        return bytes;
    }

    private ByteBuffer createBuffer() {
        return ByteBuffer.allocate(19)
            .putInt(correlationId)
            .putShort(errorCode)
            .put(apiKeysCount)
            .putShort(apiKey)
            .putShort(minVersion)
            .putShort(maxVersion)
            .put(apiTagBuffer)
            .putInt(throttledTime)
            .put(tagBuffer)
            .flip();
    }
}