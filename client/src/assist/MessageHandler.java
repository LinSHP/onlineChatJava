package assist;


import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class MessageHandler {
    private Socket socket;
    private InputStream inputStream;
    private SocketAddress socketAddress;
    private String SERVER_ADDRESS = "127.0.0.1";

    public MessageHandler() throws IOException {
        socketAddress = new InetSocketAddress(SERVER_ADDRESS, 2510);
        socket = new Socket(SERVER_ADDRESS, 2510);
        inputStream = socket.getInputStream();
    }

    public String tryLogin(String username, String password) {
        String loginMessage = String.format("<login>%s&%s\r\n", username, password);
        try {
            byte[] readBytes = new byte[1000];
            int readNum = 0;
            socket.getOutputStream().write(loginMessage.getBytes());
            while ((readNum = inputStream.read(readBytes)) != -1) {
                String isLoginSuccess = new String(readBytes);
                int messageStart = isLoginSuccess.indexOf('<');
                int messageEnd = isLoginSuccess.indexOf('>');
                isLoginSuccess = isLoginSuccess.substring(messageStart+1, messageEnd);
                if (messageStart == -1)
                    return "receive error";
                if (isLoginSuccess.compareTo("loginSuccess") == 0) {
                    return "success";
                } else if (isLoginSuccess.compareTo("loginFail") == 0) {
                    return "fail";
                } else if (isLoginSuccess.equals("hasLogin")) {
                    return "hasLogin";
                }
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return "send error";
        } catch (IOException e) {
            e.printStackTrace();
            return "network error";
        }
        return "network error";
    }

    public String tryRegister(String username, String password) {
        String registerMessage = String.format("<register>%s&%s\r\n", username, password);
        try {
            if (!socket.isConnected())
                socket.connect(socketAddress);
            byte[] readBytes = new byte[1000];
            int readNum = 0;
            socket.getOutputStream().write(registerMessage.getBytes());
            while ((readNum = inputStream.read(readBytes)) != -1) {
                String isRegisterSuccess = new String(readBytes);
                int messageStart = isRegisterSuccess.indexOf('<');
                int messageEnd = isRegisterSuccess.indexOf('>');
                if (messageStart == -1 || messageEnd == -1)
                    return "receive error";
                isRegisterSuccess = isRegisterSuccess.substring(messageStart+1, messageEnd);
                if (isRegisterSuccess.compareTo("registerSuccess") == 0) {
                    return "success";
                } else if (isRegisterSuccess.compareTo("registerFail") == 0) {
                    return "fail";
                }
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return "send error";
        } catch (IOException e) {
            e.printStackTrace();
            return "network error";
        }
        return "network error";
    }
}
