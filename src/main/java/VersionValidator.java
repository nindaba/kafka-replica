import exceptions.KafkaException;

import java.util.Set;


public class VersionValidator {
    void validate(short key, short version) throws KafkaException {
        short[] versions = ConfigConstants.API_KEY_VERSIONS.getOrDefault(key, new short[]{0,0});

        if (versions[0] < version && version > versions[1] ) {
            System.err.println("The requested api version is not supported");

            throw new KafkaException(ErrorCodes.UNSUPPORTED_VERSION);
        }
    }
}