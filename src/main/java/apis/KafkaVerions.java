package apis;

import constants.ErrorCodes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class KafkaVerions implements KafkaApi {
    public static short API_VERSIONS_KEY = (short) 18;
    private static final short[] SUPPORTED_VERSIONS = {3, 4};
    private static final byte ZERO = 0;


    @Override
    public void handle(short key, int size, InputStream in, OutputStream out) {
        try {
            var requestMessage = in.readNBytes(size);
            var requestBuffer = ByteBuffer.wrap(requestMessage);
            var responseBuffer = ByteBuffer.allocate(24);
            var verion = requestBuffer.getShort();
            var api = KafkaApiContext.get(key);

            responseBuffer.putInt(requestBuffer.getInt());


            if (isSupported(api, key, verion))
                responseBuffer.put(new byte[2]);
            else {
                var erroCode = ByteBuffer.allocate(2)
                    .putShort(ErrorCodes.UNSUPPORTED_VERSION)
                    .array();
                
                responseBuffer.put(erroCode);
            }

            responseBuffer.put((byte) 2)
                .putShort(key)
                .put(versionRange(key, api))
                .put(ZERO)
                .putInt(0)
                .put(ZERO)
                .flip()
                .array();

            out.write(ByteBuffer.allocate(4)
                .putInt(responseBuffer.remaining())
                .array());

            out.write(responseBuffer.array());

        } catch (IOException e) {
            System.err.printf("Could not validate api version %n");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isSupported(short version) {
        if (SUPPORTED_VERSIONS[0] > version || version > SUPPORTED_VERSIONS[1]) {
            System.err.println("The requested api version is not supported");

            return false;
        }

        return true;
    }

    @Override
    public short[] supportedVersions() {
        return SUPPORTED_VERSIONS;
    }


    private boolean isSupported(KafkaApi api, short key, short verion) {
        if (key == API_VERSIONS_KEY) {
            return isSupported(verion);
        }

        return api.isSupported(verion);
    }

    private byte[] versionRange(short key, KafkaApi api) {
        if (key == API_VERSIONS_KEY) {
            return versionRage();
        }

        return api.versionRage();
    }
}
