import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
  public static void main(String[] args) throws IOException {
    Socket socket = new Socket("localhost", 4444);

    InputStream in = socket.getInputStream();
    OutputStream out = socket.getOutputStream();

    while(true) {
      out.write(0);
      out.flush();
      int b = in.read();
      if (-1 == b) {
        break;
      }
    }

    in.close();
    out.close();
    socket.close();
  }
}
