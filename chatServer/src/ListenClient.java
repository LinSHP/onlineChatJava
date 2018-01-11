import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenClient {
    private ServerSocket serverSocket;

    ListenClient() throws IOException {
        serverSocket = new ServerSocket(2510);
        //serverSocket.bind(new InetSocketAddress("127.0.0.1" ,2510));
    }

    public void listen() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            HandleClientThread clientThread = new HandleClientThread(socket);
            clientThread.start();
        }
    }

    public static void main(String[] args) {
        try {
            ListenClient listenClient = new ListenClient();
            listenClient.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
