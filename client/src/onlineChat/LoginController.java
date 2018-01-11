package onlineChat;

import assist.JdbcTemplate;
import assist.MessageHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Main application;
    private MessageHandler messageHandler;
    private boolean connectSuccess;

    @FXML
    private Button loginButton, registerButton;

    @FXML
    private TextField usernameField, passwordField;

    @FXML
    private CheckBox loadPrevious;

    public LoginController() throws IOException {
        super();
        try {
            messageHandler = new MessageHandler();
            connectSuccess = true;
        } catch (Exception e) {
            connectSuccess = false;
        }
    }

    public void setApplication(Main application) {
        this.application = application;
    }

    private void checkConnectStatus() {
        if (!connectSuccess)
            popUpMessage("网络错误，请重启程序");
    }

    public void checkLogin() {
        checkConnectStatus();
        System.out.println("Button clicked");
        String username = usernameField.getText();
        String password = passwordField.getText();
        String isLoginSuccess = messageHandler.tryLogin(username, password);
        if (isLoginSuccess.compareTo("success") == 0) {
            application.gotoMain(username, loadPrevious.isSelected());
        } else if (isLoginSuccess.compareTo("fail") == 0) {
            popUpMessage("用户名或密码错误");
        } else if (isLoginSuccess.equals("hasLogin")) {
            popUpMessage("该用户已登录");
        } else {
            popUpMessage("网络错误");
        }
    }

    public void register() {
        checkConnectStatus();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String isRegisterSuccess = messageHandler.tryRegister(username, password);
        if (isRegisterSuccess.compareTo("success") == 0) {
            popUpMessage("注册成功");
        } else if (isRegisterSuccess.compareTo("fail") == 0) {
            popUpMessage("注册失败\n用户名已被注册");
        } else {
            popUpMessage("网络错误");
        }
    }

    private void popUpMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void initialize(URL url, ResourceBundle rb) {}

}
