package ai.jet.kafka.clients;

import ai.jet.kafka.apis.KafkaApiContext;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public record KafkaClient(InputStream in, OutputStream out) implements Runnable {

    @Override
    public void run() {
        try {
            var messageSize = new byte[4];

            while (in.read(messageSize) == 4) {
                short key = wrap(in.readNBytes(2))
                    .getShort();

                int size = wrap(messageSize).getInt() - 2;

                KafkaApiContext.get(key)
                    .handle(key, size, in, out);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ByteBuffer wrap(byte[] bytes) throws IOException {
        return ByteBuffer.wrap(bytes);
    }
}
