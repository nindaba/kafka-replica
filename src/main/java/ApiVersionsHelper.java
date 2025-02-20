import java.nio.ByteBuffer;

public class ApiVersionsHelper {
    byte[] versionRange(short apiVersion) {
        short[] minMax = ConfigConstants.API_KEY_VERSIONS.getOrDefault(apiVersion,new short[]{0,0});

        return ByteBuffer.allocate(4)
                .putShort(minMax[0])
                .putShort(minMax[1])
                .array();
    }
}