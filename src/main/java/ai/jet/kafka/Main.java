package ai.jet.kafka;

import ai.jet.kafka.constants.KafkaServerConfig;
import ai.jet.kafka.server.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        var server = new Server();
        server.start();
    }
}

