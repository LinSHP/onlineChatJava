import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HandleClientThread extends Thread {
    private static List<String> globalMessageQueue = new LinkedList<>();
    private static Map<String, Boolean> hasLogin = new HashMap<>();
    private int currentIndex;
    private Socket socket;
    private JdbcTemplate jdbcTemplate;
    private String username;

    HandleClientThread(Socket socket) throws IOException {
        this.socket = socket;
        jdbcTemplate = new JdbcTemplate();
    }

    private void handleLogin(String message) {
        String[] args = message.split("&");
        String username = args[0];
        String password = args[1];
        boolean success = jdbcTemplate.checkUser(username, password);
        try {
            if (hasLogin.getOrDefault(username, false)) {
                socket.getOutputStream().write("<hasLogin>".getBytes());
            } else if (success) {
                socket.getOutputStream().write("<loginSuccess>".getBytes());
                this.username = username;
                synchronized (this) {
                    hasLogin.put(username, true);
                }
            } else {
                socket.getOutputStream().write("<loginFail>".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRegister(String message) {
        String username = message.substring(0, message.indexOf('&'));
        String password = message.substring(message.indexOf('&')+1);
        boolean hasRegister = jdbcTemplate.checkHasRegister(username);
        try {
            if (hasRegister) {
                socket.getOutputStream().write("<registerFail>".getBytes());
            } else {
                if (jdbcTemplate.register(username, password)) {
                    socket.getOutputStream().write("<registerSuccess>".getBytes());
                } else {
                    socket.getOutputStream().write("<registerFail>".getBytes());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleUpdateChatArea(String loadPrevious) {
        if (loadPrevious.equals("false"))
            currentIndex = globalMessageQueue.size();
        while (true) {
            try {
                //System.out.println(String.format("%d %d", currentIndex, globalMessageQueue.size()));
                Thread.sleep(100);
                if (currentIndex != globalMessageQueue.size() && globalMessageQueue.size() != 0) {
                    //System.out.println(globalMessageQueue.get(currentIndex));
                    socket.getOutputStream().write(globalMessageQueue.get(currentIndex).getBytes());
                    currentIndex++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    synchronized private void handleSend(String message) {
        System.out.println("send " + message);
        globalMessageQueue.add(message);
    }

    public void run() {
        try {
            int readNum;
            byte[] readBytes = new byte[1000];
            while ((readNum = socket.getInputStream().read(readBytes)) != -1) {
                String readMessage = new String(readBytes);
                String command = readMessage.substring(readMessage.indexOf('<')+1, readMessage.indexOf('>'));
                if (command.equals("login")) {
                    handleLogin(readMessage.substring(readMessage.indexOf('>')+1, readMessage.indexOf("\r\n")));
                } else if (command.equals("register")) {
                    handleRegister(readMessage.substring(readMessage.indexOf('>')+1, readMessage.indexOf("\r\n")));
                } else if (command.equals("updateChatArea")) {
                    handleUpdateChatArea(readMessage.substring(readMessage.indexOf('>')+1, readMessage.indexOf("\r\n")));
                } else if (command.equals("send")) {
                    handleSend(readMessage.substring(readMessage.indexOf('>')+1, readMessage.indexOf("\r\n")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            hasLogin.put(username, false);
        }
    }
}
