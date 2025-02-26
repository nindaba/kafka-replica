package ai.jet.kafka.apis;

import ai.jet.kafka.apis.response.ApiVersionResponse;
import ai.jet.kafka.constants.ErrorCodes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class KafkaVerions implements KafkaApi {
    public static short API_VERSIONS_KEY = (short) 18;
    private static final short[] SUPPORTED_VERSIONS = {3, 4};
    private static final byte ZERO = 0;


    @Override
    public void handle(short key, int size, InputStream in, OutputStream out) {
        try {
            var requestBuffer = ByteBuffer.wrap(in.readNBytes(size));
            var verion = requestBuffer.getShort();
            var response = ApiVersionResponse.of(requestBuffer.getInt());
            var api = KafkaApiContext.get(key);

            if (!isSupported(api, key, verion)) {
                response.errorCode(ErrorCodes.UNSUPPORTED_VERSION);
            }

            out.write(response
                .minVersion(SUPPORTED_VERSIONS[0])
                .maxVersion(SUPPORTED_VERSIONS[1])
                .getBytes());

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
