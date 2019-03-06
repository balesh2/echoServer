import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(4444);

    while (true) {
      Socket clientConnection = serverSocket.accept();

      InputStream in = clientConnection.getInputStream();
      OutputStream out = clientConnection.getOutputStream();

      int b;
      while ((b = in.read()) != -1) {
        out.write(b);
        out.flush();
      }

      out.close();
      in.close();
      clientConnection.close();
    }
  }
}
