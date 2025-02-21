import apis.KafkaApiContext;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public record KafkaClient(Socket clientSocket) implements Runnable {

    @Override
    public void run() {
        try {
            var in = clientSocket.getInputStream();
            var out = clientSocket.getOutputStream();

            var messageSize = new byte[4];

            if (in.read(messageSize) == 4) {
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
