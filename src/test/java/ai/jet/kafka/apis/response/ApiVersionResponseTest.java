package ai.jet.kafka.apis.response;

import ai.jet.kafka.apis.KafkaVerions;
import ai.jet.kafka.constants.ErrorCodes;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiVersionResponseTest {

    @Test
    void shouldGenerateValidVersion3Response() {
        // Given
        int correlationId = 123;
        short errorCode = ErrorCodes.NONE;
        short minVersion = 3;
        short maxVersion = 4;

        // When
        byte[] response = ApiVersionResponse.of(correlationId)
                .errorCode(errorCode)
                .minVersion(minVersion)
                .maxVersion(maxVersion)
                .getBytes();

        // Then
        ByteBuffer buffer = ByteBuffer.wrap(response)
                .order(ByteOrder.BIG_ENDIAN);

        assertEquals(19, buffer.getInt());  // response size
        assertEquals(correlationId, buffer.getInt());  // correlation id
        assertEquals(errorCode, buffer.getShort());  // error code
        assertEquals(2, buffer.get());  // num_api_keys
        assertEquals(KafkaVerions.API_VERSIONS_KEY, buffer.getShort());  // api_key
        assertEquals(minVersion, buffer.getShort());  // min_version
        assertEquals(maxVersion, buffer.getShort());  // max_version
        assertEquals(0, buffer.get());  // api_tag_buffer
        assertEquals(0, buffer.getInt());  // throttle_time_ms
        assertEquals(0, buffer.get());  // tag_buffer
        assertEquals(0, buffer.remaining());
    }

    @Test
    void shouldGenerateValidVersion4Response() {
        // Given
        int correlationId = 456;
        short errorCode = ErrorCodes.UNSUPPORTED_VERSION;
        short minVersion = 3;
        short maxVersion = 4;

        // When
        byte[] response = ApiVersionResponse.of(correlationId)
                .errorCode(errorCode)
                .minVersion(minVersion)
                .maxVersion(maxVersion)
                .getBytes();

        // Then
        ByteBuffer buffer = ByteBuffer.wrap(response)
                .order(ByteOrder.BIG_ENDIAN);

        assertEquals(19, buffer.getInt());  // response size
        assertEquals(correlationId, buffer.getInt());  // correlation id
        assertEquals(errorCode, buffer.getShort());  // error code
        assertEquals(2, buffer.get());  // num_api_keys
        assertEquals(KafkaVerions.API_VERSIONS_KEY, buffer.getShort());  // api_key
        assertEquals(minVersion, buffer.getShort());  // min_version
        assertEquals(maxVersion, buffer.getShort());  // max_version
        assertEquals(0, buffer.get());  // api_tag_buffer
        assertEquals(0, buffer.getInt());  // throttle_time_ms
        assertEquals(0, buffer.get());  // tag_buffer
        assertEquals(0, buffer.remaining());
    }
}