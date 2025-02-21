package apis;

import java.util.HashMap;
import java.util.Map;

public class KafkaApiContext {
    private static final Map<Short, KafkaApi> APIS = new HashMap<>();

    static {
        APIS.put((short) 18, Apis.VERIONS_API);
    }


    public static KafkaApi get(short apiKey) {
        return APIS.getOrDefault(apiKey, Apis.VERIONS_API);
    }

    private interface Apis {
        KafkaApi VERIONS_API = new KafkaVerions();
    }
}

