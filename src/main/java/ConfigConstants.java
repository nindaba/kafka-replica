import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigConstants {
    static short[] SUPPORTED_VERSOINS = {3,4};

    static Map<Short, short[]> API_KEY_VERSIONS;

    static {
        API_KEY_VERSIONS = new HashMap<>();

        API_KEY_VERSIONS.put((short) 18,SUPPORTED_VERSOINS);
    }

}