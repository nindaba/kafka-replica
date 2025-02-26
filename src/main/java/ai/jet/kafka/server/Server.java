package ai.jet.kafka.server;

import ai.jet.kafka.clients.KafkaClient;
import ai.jet.kafka.constants.KafkaServerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class Server {
    private static final Logger LOG = LogManager.getLogger(Server.class);

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(KafkaServerConfig.DEFAULT_BROKER_PORT)) {
            serverSocket.setReuseAddress(true);

            LOG.info("Broker started listening on port: {}", KafkaServerConfig.DEFAULT_BROKER_PORT);

            processClients(serverSocket);
        } catch (
            IOException e) {
            LOG.error("Could not start the server due", e);
        }
    }

    private void processClients(ServerSocket serverSocket) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Socket client;
            while ((client = serverSocket.accept()) != null)
                executor.execute(new KafkaClient(client.getInputStream(), client.getOutputStream()));

        } catch (IOException e) {
            LOG.error("Could not connect to client due", e);
        }
    }
}