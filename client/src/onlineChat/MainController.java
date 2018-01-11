package onlineChat;

import assist.ChatAreaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    private Main application;
    private String username;
    private ChatAreaService chatAreaService;
    private boolean loadPrevious;

    @FXML
    Button sendButton;

    @FXML
    ScrollPane chatPane;

    @FXML
    TextArea inputArea, chatArea;

    public MainController() {
        super();
    }

    public void setApplication(Main application) {
        this.application = application;
        inputArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessage();
            }
        });
    }

    public void setUsername(String username) {
        try {
            chatArea.setEditable(false);
            chatAreaService = new ChatAreaService(chatArea, loadPrevious);
            this.username = username;
            chatAreaService.updateChatArea();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLoadPrevious(boolean loadPrevious) {
        this.loadPrevious = loadPrevious;
    }

    public void sendMessage() {
        String message = inputArea.getText();
        if (!message.replaceAll("\n", "").equals("")) {
            chatAreaService.trySend(username, message);
            inputArea.setText("");
        }
    }

    public void initialize(URL url, ResourceBundle rb) {}
}
