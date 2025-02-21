package apis;


import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public interface KafkaApi {
    void handle(short key, int size, InputStream in, OutputStream out);
    short key();
    boolean isSupported(short version);
    short[] supportedVersions();

    default byte[] versionRage() {
        var versions = supportedVersions();
        return ByteBuffer.allocate(4)
                .putShort(versions[0])
                .putShort(versions[1])
                .array();
    }

}
