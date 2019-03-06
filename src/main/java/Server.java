import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  private static int messageSize = 1000;

  public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(4444);

    while (true) {
      try {
        Socket clientConnection = serverSocket.accept();

        new Thread(()-> {

          try {
            InputStream in = clientConnection.getInputStream();
            OutputStream out = clientConnection.getOutputStream();

            byte[] buffer = new byte[messageSize];
            int read = 0;
            while ((read = in.read(buffer)) != -1) {
//              out.write(buffer, 0, read);
//              out.flush();
            }

            out.close();
            in.close();
            clientConnection.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }).start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
