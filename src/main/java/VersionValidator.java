import exceptions.KafkaException;


public class VersionValidator {
    void validate(short version) throws KafkaException {
        if (!ConfigConstants.SUPPORTED_VERSOINS.contains((int) version)) {
            System.out.println("The requested api version is not supported");

            throw new KafkaException(ErrorCodes.UNSUPPORTED_VERSION);
        }
    }
}