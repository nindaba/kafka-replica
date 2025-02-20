import java.nio.ByteBuffer;

public class ApiVersionsHelper {
    byte[] versionRange(short apiVersion) {
        short[] minMax = ConfigConstants.API_KEY_VERSIONS.get(apiVersion);

        return ByteBuffer.allocate(2)
                .putShort(minMax[0])
                .putShort(minMax[1])
                .array();
    }
}