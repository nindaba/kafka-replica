import exceptions.KafkaException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Main {
    private static final VersionValidator versionValidator = new VersionValidator();
    private static final ApiVersionsHelper apiVersionHelper = new ApiVersionsHelper();

    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.err.println("Logs from your program will appear here!");

        // Uncomment this block to pass the first stage

        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 9092;
        try {
            serverSocket = new ServerSocket(port);
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            // Wait for connection from client.
            clientSocket = serverSocket.accept();

            var in = clientSocket.getInputStream();
            var out = clientSocket.getOutputStream();

            byte[] message = new byte[2];
            byte[] requestMessageSize = in.readNBytes(4);
            byte[] apiKey = in.readNBytes(2);
            byte[] apiVersion = in.readNBytes(2);
            byte[] correlationId = in.readNBytes(4);

            short key = ByteBuffer.wrap(apiKey).getShort();
            short version = ByteBuffer.wrap(apiVersion).getShort();

            try {
                versionValidator.validate(key, version);

            } catch (KafkaException e) {
                message = e.errorCode();
            }

            byte[] messageSize = ByteBuffer.allocate(4)
                .putInt((4 +2 +1 +2 +4 +1 +4 +1))
                .array();


            out.write(messageSize);
            out.write(correlationId);                           /* 4 bytes*/
            out.write(message);                                 /* 2 bytes  The top-level error code.*/
            out.write(new byte[]{1 + 1});                       /* 1 byte   The APIs supported by the broker.*/
            out.write(apiKey);                                  /* 2 bytes  Api index */
            out.write(apiVersionHelper.versionRange(key));      /* 4 bytes  min + max versions */
            out.write(new byte[1]);                             /* 1 byte   api tag_buffer of tagged fields*/
            out.write(new byte[4]);                             /* 4 bytes  The duration in milliseconds for which the request was throttled due to a quota violation, or zero if the request did not violate any quota. */
            out.write(new byte[1]);                             /* 1 byte   tagged fields */

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }
}
