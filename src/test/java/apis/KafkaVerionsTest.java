package apis;

import ai.jet.kafka.apis.KafkaVerions;
import ai.jet.kafka.constants.ErrorCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.*;

class KafkaVerionsTest {
    private static final short API_VERSION_MESSAGE_SIZE_V3 = 18;
    private static final short API_VERSION_MESSAGE_SIZE_V4 = 19;
    private KafkaVerions kafkaVerions;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        kafkaVerions = new KafkaVerions();
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    void shouldHandleValidApiVersion3Response() {
        // Given
        short apiKey = KafkaVerions.API_VERSIONS_KEY;
        short apiVersion = 3;
        int correlationId = 123;
        ByteBuffer requestBuffer = buffer(12)
                .putShort(apiVersion)
                .putInt(correlationId);

        InputStream inputStream = new  ByteArrayInputStream(requestBuffer.array());

        // When
        kafkaVerions.handle(apiKey, 8, inputStream, outputStream);

        // Then
        ByteBuffer responseBuffer = ByteBuffer.wrap(outputStream.toByteArray());

        assertEquals( API_VERSION_MESSAGE_SIZE_V3, responseBuffer.getInt());  // response size
        assertEquals(correlationId, responseBuffer.getInt());  // correlation id
        assertEquals(ErrorCodes.NONE, responseBuffer.getShort());  // error code
        assertEquals(2, responseBuffer.get());  // num_api_keys
        assertEquals(apiKey, responseBuffer.getShort());  // api_key
        assertEquals(3, responseBuffer.getShort());  // min_version
        assertEquals(4, responseBuffer.getShort());  // max_version
        assertEquals(0, responseBuffer.get());  // TAG_BUFFER
        assertEquals(0, responseBuffer.getInt());  // throttle_time_ms
        assertEquals(0, responseBuffer.remaining());
    }


    @Test
    void shouldHandleValidApiVersion4Response() {
        // Given
        short apiKey = KafkaVerions.API_VERSIONS_KEY;
        short apiVersion = 4;
        int correlationId = 12342482;
        ByteBuffer requestBuffer = buffer(12)
                .putShort(apiVersion)
                .putInt(correlationId);

        InputStream inputStream = new  ByteArrayInputStream(requestBuffer.array());

        // When
        kafkaVerions.handle(apiKey, 8, inputStream, outputStream);

        // Then
        ByteBuffer responseBuffer = ByteBuffer.wrap(outputStream.toByteArray());

        assertEquals( API_VERSION_MESSAGE_SIZE_V4, responseBuffer.getInt());  // response size
        assertEquals(correlationId, responseBuffer.getInt());  // correlation id
        assertEquals(ErrorCodes.NONE, responseBuffer.getShort());  // error code
        assertEquals(2, responseBuffer.get());  // num_api_keys
        assertEquals(apiKey, responseBuffer.getShort());  // api_key
        assertEquals(3, responseBuffer.getShort());  // min_version
        assertEquals(4, responseBuffer.getShort());  // max_version
        assertEquals(0, responseBuffer.get());  // TAG_BUFFER
        assertEquals(0, responseBuffer.getInt());  // throttle_time_ms
        assertEquals(0, responseBuffer.get());  // TAG_BUFFER
        assertEquals(0, responseBuffer.remaining());
    }

    @Test
    void shouldHandleUnsupportedApiVersionWithResponseVersion4() {
        // Given
        short apiKey = KafkaVerions.API_VERSIONS_KEY;
        short apiVersion = 5;  // unsupported version
        int correlationId = 456;
        ByteBuffer requestBuffer = buffer(12)
                .putShort(apiVersion)
                .putInt(correlationId);
        InputStream inputStream = new  ByteArrayInputStream(requestBuffer.array());

        // When
        kafkaVerions.handle(apiKey, 8, inputStream, outputStream);

        // Then
        ByteBuffer responseBuffer = ByteBuffer.wrap(outputStream.toByteArray())
                .order(ByteOrder.BIG_ENDIAN);

        assertEquals(API_VERSION_MESSAGE_SIZE_V4, responseBuffer.getInt());  // response size
        assertEquals(correlationId, responseBuffer.getInt());  // correlation id
        assertEquals(ErrorCodes.UNSUPPORTED_VERSION, responseBuffer.getShort());  // error code (UNSUPPORTED_VERSION)
        assertEquals(2, responseBuffer.get());  // num_api_keys
        assertEquals(apiKey, responseBuffer.getShort());  // api_key
        assertEquals(3, responseBuffer.getShort());  // min_version
        assertEquals(4, responseBuffer.getShort());  // max_version
        assertEquals(0, responseBuffer.get());  // TAG_BUFFER
        assertEquals(0, responseBuffer.getInt());  // throttle_time_ms
        assertEquals(0, responseBuffer.get());  // TAG_BUFFER
        assertEquals(0, responseBuffer.remaining());

    }

    @Test
    void shouldHandleValidApiVersionWithResponseVersion4() {
        // Given
        short apiKey = KafkaVerions.API_VERSIONS_KEY;
        short apiVersion = -3;
        int correlationId = 123;
        ByteBuffer requestBuffer = buffer(12)
                .putShort(apiVersion)
                .putInt(correlationId);

        InputStream inputStream = new  ByteArrayInputStream(requestBuffer.array());

        // When
        kafkaVerions.handle(apiKey, 8, inputStream, outputStream);

        // Then
        ByteBuffer responseBuffer = ByteBuffer.wrap(outputStream.toByteArray());

        assertEquals( API_VERSION_MESSAGE_SIZE_V4, responseBuffer.getInt());  // response size
        assertEquals(correlationId, responseBuffer.getInt());  // correlation id
        assertEquals(ErrorCodes.UNSUPPORTED_VERSION, responseBuffer.getShort());  // error code
        assertEquals(2, responseBuffer.get());  // num_api_keys
        assertEquals(apiKey, responseBuffer.getShort());  // api_key
        assertEquals(3, responseBuffer.getShort());  // min_version
        assertEquals(4, responseBuffer.getShort());  // max_version
        assertEquals(0, responseBuffer.get());  // TAG_BUFFER
        assertEquals(0, responseBuffer.getInt());  // throttle_time_ms
        assertEquals(0, responseBuffer.get());  // TAG_BUFFER
        assertEquals(0, responseBuffer.remaining());

    }

    @Test
    void shouldHandleHigherApiVersionWithResponseVersion4() {
        // Given
        short apiKey = KafkaVerions.API_VERSIONS_KEY;
        short apiVersion = 5;  // unsupported version
        int correlationId = 456;
        ByteBuffer requestBuffer = buffer(12)
                .putShort(apiVersion)
                .putInt(correlationId);
        InputStream inputStream = new  ByteArrayInputStream(requestBuffer.array());

        // When
        kafkaVerions.handle(apiKey, 8, inputStream, outputStream);

        // Then
        ByteBuffer responseBuffer = ByteBuffer.wrap(outputStream.toByteArray())
                .order(ByteOrder.BIG_ENDIAN);

        assertEquals(API_VERSION_MESSAGE_SIZE_V4, responseBuffer.getInt());  // response size
        assertEquals(correlationId, responseBuffer.getInt());  // correlation id
        assertEquals(ErrorCodes.UNSUPPORTED_VERSION, responseBuffer.getShort());  // error code (UNSUPPORTED_VERSION)
        assertEquals(2, responseBuffer.get());  // num_api_keys
        assertEquals(apiKey, responseBuffer.getShort());  // api_key
        assertEquals(3, responseBuffer.getShort());  // min_version
        assertEquals(4, responseBuffer.getShort());  // max_version
        assertEquals(0, responseBuffer.get());  // TAG_BUFFER
        assertEquals(0, responseBuffer.getInt());  // throttle_time_ms
        assertEquals(0, responseBuffer.get());  // TAG_BUFFER
        assertEquals(0, responseBuffer.remaining());
    }


    private ByteBuffer buffer(int capacity) {
        return ByteBuffer.allocate(capacity)
                .order(ByteOrder.BIG_ENDIAN);
    }
}
