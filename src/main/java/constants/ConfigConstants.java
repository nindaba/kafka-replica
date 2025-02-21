package constants;

import java.util.HashMap;
import java.util.Map;

public class ConfigConstants {
    static short[] SUPPORTED_VERSOINS = {3, 4};

    public static Map<Short, short[]> API_KEY_VERSIONS;

    static {
        API_KEY_VERSIONS = new HashMap<>();

        API_KEY_VERSIONS.put(ApiKeys.API_VERSIONS_KEY, SUPPORTED_VERSOINS);
    }

    public interface ApiKeys {
        static short API_VERSIONS_KEY = (short) 18;
    }

}
