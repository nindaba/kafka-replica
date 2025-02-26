package ai.jet.kafka.apis;

import ai.jet.kafka.constants.ErrorCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;
class KafkaVersionsHexTest {
    private static final short API_VERSION_MESSAGE_SIZE_V3 = 18;
    private static final short API_VERSION_MESSAGE_SIZE_V4 = 19;
    private KafkaVerions kafkaVerions;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setUp() {
        kafkaVerions = new KafkaVerions();
        out = new ByteArrayOutputStream();
    }

    @Test
    void shouldHandleInvalidApiVersionRequest() throws IOException {
        var in = stream("00 00 00 23 00 12 d1 05 20 86 a0 95 00 09 6b 61");
        var size = wrap(in.readNBytes(4)).getInt();
        var key = wrap(in.readNBytes(2)).getShort();

        // When
        kafkaVerions.handle(key, size, in, out);

        // Then
        ByteBuffer responseBuffer = wrap(out.toByteArray());

        assertEquals(API_VERSION_MESSAGE_SIZE_V4, responseBuffer.getInt());  // response size
        assertEquals(0x2086a095, responseBuffer.getInt());  // correlation id
        assertEquals(ErrorCodes.UNSUPPORTED_VERSION, responseBuffer.getShort());  // error code
        assertEquals(2, responseBuffer.get());  // num_api_keys
        assertEquals(KafkaVerions.API_VERSIONS_KEY, responseBuffer.getShort());  // api_key
        assertEquals(3, responseBuffer.getShort());  // min_version
        assertEquals(4, responseBuffer.getShort());  // max_version
        assertEquals(0, responseBuffer.get());  // TAG_BUFFER
        assertEquals(0, responseBuffer.getInt());  // throttle_time_ms
        assertEquals(0, responseBuffer.get());  // TAG_BUFFER
        assertEquals(0, responseBuffer.remaining());
    }

    @Test
    void shouldHandle_api_18_version_4_Request() throws IOException {
        var in = stream("00 00 00 23 00 12 00 04 4c e9 f9 17 00 09 6b 61 66 6b 61 2d 63 6c 69 00 0a 6b 61 66 6b 61 2d 63 6c 69 04 30 2e 31 00");
        var size = wrap(in.readNBytes(4)).getInt();
        var key = wrap(in.readNBytes(2)).getShort();

        // When
        kafkaVerions.handle(key, size, in, out);

        // Then
        ByteBuffer responseBuffer = wrap(out.toByteArray());

        assertEquals(API_VERSION_MESSAGE_SIZE_V4, responseBuffer.getInt());  // response size
        assertEquals(0x4ce9f917, responseBuffer.getInt());  // correlation id
        assertEquals(ErrorCodes.NONE, responseBuffer.getShort());  // error code
        assertEquals(1 + 1, responseBuffer.get());  // num_api_keys
        assertEquals(KafkaVerions.API_VERSIONS_KEY, responseBuffer.getShort());  // api_key
        assertEquals(3, responseBuffer.getShort());  // min_version
        assertEquals(4, responseBuffer.getShort());  // max_version
        assertEquals(0, responseBuffer.get());  // TAG_BUFFER
        assertEquals(0, responseBuffer.getInt());  // throttle_time_ms
        assertEquals(0, responseBuffer.get());  // TAG_BUFFER
        assertEquals(0, responseBuffer.remaining());
    }

    private static ByteArrayInputStream stream(String hex) {
        hex = hex.replace(" ", "");

        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                + Character.digit(hex.charAt(i + 1), 16));
        }

        return new ByteArrayInputStream(data);
    }


    private ByteBuffer wrap(byte[] bytes) throws IOException {
        return ByteBuffer.wrap(bytes);
    }
}