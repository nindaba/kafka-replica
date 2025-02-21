package apis;

import java.util.HashMap;
import java.util.Map;

public class KafkaApiContext {
    private static final Map<Short, KafkaApi> APIS = new HashMap<>();
    private static final KafkaApi VERIONS_API;

    static {
        VERIONS_API = new KafkaVerions();
        
        APIS.put(KafkaVerions.API_VERSIONS_KEY, VERIONS_API);
    }


    public static KafkaApi get(short apiKey) {
        return APIS.getOrDefault(apiKey, VERIONS_API);
    }
}

