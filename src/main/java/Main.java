import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.err.println("Logs from your program will appear here!");

        // Uncomment this block to pass the first stage

        int port = 9092;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            // Wait for connection from client.
            try (Socket clientSocket = serverSocket.accept()) {
                var client = new KafkaClient(clientSocket);
                client.run();
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        } catch (
                IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
