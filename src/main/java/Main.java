import exceptions.KafkaException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Main {
    private static final VersionValidator versionValidator = new VersionValidator();

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
            int messageSizeAndHeaderLength = 12;
            byte[] messageSizeAndHeader = new byte[messageSizeAndHeaderLength];
            byte[] correlationId = new byte[4];
            byte[] messageSize;
            byte[] message = new byte[0];


            int length = in.read(messageSizeAndHeader);

            if (length < messageSizeAndHeaderLength) {
                throw new IOException("Could not read the request message size is %s less than 8 bytes".formatted(length));
            }


            try {
                short version = ByteBuffer.wrap(messageSizeAndHeader).getShort(6);
                versionValidator.validate(version);

            } catch (KafkaException e) {
                message = e.errorCode();
            }

            ByteBuffer.wrap(messageSizeAndHeader) .get(8, correlationId);

            messageSize = ByteBuffer.allocate(4)
                    .putInt(message.length + 4)
                    .array();

            out.write(messageSize);
            out.write(correlationId);
            out.write(message);

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

//public static void main(String[] args){
//    byte[] b = {1,1,1,1};
//    System.out.println(ByteBuffer.wrap(b)..getInt());
//}
