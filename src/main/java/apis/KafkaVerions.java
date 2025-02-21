package apis;

import constants.ErrorCodes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;


public class KafkaVerions implements KafkaApi {
    public static short API_VERSIONS_KEY = (short) 18;
    private static final short[] SUPPORTED_VERSIONS = {3, 4};
    private static final byte ZERO = 0;

    private static final Logger log = LogManager.getLogger(KafkaVerions.class);


    @Override
    public void handle(short key, int size, InputStream in, OutputStream out) {
        try {
            var requestMessage = in.readNBytes(size);
            var requestBuffer = ByteBuffer.wrap(requestMessage);
            var responseBuffer = buffer(24);
            var erroCode = buffer(2);
            var verion = requestBuffer.getShort();
            var correlationId = requestBuffer.getInt();

            var api = KafkaApiContext.get(key);

            log.info("Received request with correlation ID: {} for api versions for : {} , and checking support for version: {}", correlationId, key, verion);

            if (isSupported(api, key, verion))
                erroCode.putShort(ErrorCodes.NONE);
            else {
                log.error("Unsupported api: {} version: {} with correlation ID: {}", key, verion, correlationId);
                erroCode.putShort(ErrorCodes.UNSUPPORTED_VERSION);
            }

            log.info("Correlation id: {}", correlationId);

            responseBuffer.putInt(correlationId)
                    .put(erroCode.array())
                    .put((byte) 2)
                    .putShort(key)
                    .put(versionRange(key, api))
                    .put(ZERO)
                    .putInt(0);


            if (verion != 3)
                responseBuffer.put(ZERO);

            responseBuffer.flip();

            var responseSize = responseBuffer.remaining();
            var responseMessage = new byte[responseSize];

            responseBuffer.get(responseMessage);


            log.info("Versions Api response for correlation ID :{} with message size of {} bytes", correlationId, responseMessage.length);

            out.write(buffer(4)
                    .putInt(responseSize)
                    .array());

            out.write(responseMessage);

            out.flush();
        } catch (IOException e) {
            System.err.printf("Could not validate api version %n");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isSupported(short version) {
        return SUPPORTED_VERSIONS[0] <= version && version <= SUPPORTED_VERSIONS[1];
    }

    @Override
    public short[] supportedVersions() {
        return SUPPORTED_VERSIONS;
    }

    @Override
    public short key() {
        return API_VERSIONS_KEY;
    }

    private boolean isSupported(KafkaApi api, short key, short verion) {
        if (api.key() == key) {
            return api.isSupported(verion);
        }

        return false;
    }

    private byte[] versionRange(short key, KafkaApi api) {
        if (api.key() == key) {
            return api.versionRage();
        }

        return new byte[]{0, 0, 0, 0};
    }

    private ByteBuffer buffer(int capacity) {
        return ByteBuffer.allocate(capacity)
                .order(ByteOrder.BIG_ENDIAN);
    }
}
