import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.LongAdder;

public class Client {
  static volatile boolean running = true;
  static LongAdder sent = new LongAdder();
  static LongAdder received = new LongAdder();
  static boolean bidirectional = false;
  static int packetSize = 1000;
  static int threadCount = 100;

  public static void main(String[] args) throws IOException, InterruptedException {

    Thread statSampler = new Thread(() -> {
      long previousSent = 0;
      long previousReceived = 0;

      while (running) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
          break;
        }

        long currentSent = sent.sum();
        long currentReceived = received.sum();
        System.out.println("sent: " + (currentSent - previousSent) + ", received: " + (currentReceived - previousReceived));
        previousSent = currentSent;
        previousReceived = currentReceived;
      }
    });
    statSampler.start();

    for (int i = 0; i < threadCount; i++) {

      new Thread(() -> {
        try {
          Socket socket = new Socket("localhost", 4444);

          InputStream in = socket.getInputStream();
          OutputStream out = socket.getOutputStream();

          byte[] buffer = new byte[packetSize];

          while (true) {
            out.write(buffer);
            sent.add(packetSize);

            if (bidirectional) {
              int read = in.read(buffer);
              if (-1 == read) {
                break;
              }
              received.add(read);
            }
          }

          in.close();
          out.close();
          socket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }).start();
    }

    while (true) {
      Thread.sleep(1000);
    }

//    running = false;
//    statSampler.join();
  }
}
