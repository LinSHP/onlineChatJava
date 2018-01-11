package assist;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.Date;

public class ChatAreaService extends Thread {
    private Socket sendSocket, receiveSocket;
    private SocketAddress socketAddress;
    private TextArea chatArea;
    private String SERVER_ADDRESS = "127.0.0.1";

    public ChatAreaService(TextArea chatArea, boolean loadPrevious) throws IOException {
        socketAddress = new InetSocketAddress(SERVER_ADDRESS, 2510);
        sendSocket = new Socket(SERVER_ADDRESS, 2510);
        receiveSocket = new Socket(SERVER_ADDRESS, 2510);
        receiveSocket.getOutputStream().write(String.format("<updateChatArea>%s\r\n", loadPrevious).getBytes());
        this.chatArea = chatArea;
    }

    public String trySend(String username, String content) {
        String sendMessage = String.format("<send>%s:%s\n%s\n\r\n", new Date(), username, content);
        try {
            sendSocket.getOutputStream().write(sendMessage.getBytes());
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            return "network error";
        }
    }

    public void updateChatArea() {
        Service service = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        while (!isCancelled()) {
                            int readNum;
                            byte[] readBytes = new byte[10000];
                            while ((readNum = receiveSocket.getInputStream().read(readBytes)) != -1) {
                                String totalMessage = new String(readBytes, 0, readNum);
                                //System.out.println(totalMessage);
                                chatArea.appendText(totalMessage + '\n');
                            }
                        }
                        return null;
                    }
                };
            }
        };
        service.start();
    }
}
