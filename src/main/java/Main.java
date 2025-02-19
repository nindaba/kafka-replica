import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Scanner;

public class Main {
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

            int headerSize = 12;

            byte[] messageSizeAndHeader = new byte[headerSize];


            int length = in.read(messageSizeAndHeader);

            if(length < headerSize){
                throw new IOException("Could not read the request message size is %s less than 8 bytes".formatted(length));
            }

            var out = clientSocket.getOutputStream();

            byte[] message = "This is my message to kafka".getBytes();
            byte[] correlationId = new byte[4];
            byte[] messageSize = new byte[4];

            ByteBuffer.wrap(messageSizeAndHeader).get(8,correlationId);



            int size = message.length + correlationId.length;

            for(int i = 0; i < 4; i++)
                messageSize[3-i] = (byte) (size << 8*i);

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
