package onlineChat;

import assist.MessageHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;

public class Main extends Application {
    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        primaryStage.setTitle("登录");
        gotoLogin();
        primaryStage.show();
    }

    public void gotoLogin() {
        try {
            LoginController loginController = (LoginController) replaceSceneContent("login.fxml", "登录");
            loginController.setApplication(this);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void gotoMain(String username, boolean loadPrevious) {
        try {
            MainController mainController = (MainController) replaceSceneContent("main.fxml", "聊天室");
            mainController.setApplication(this);
            mainController.setLoadPrevious(loadPrevious);
            mainController.setUsername(username);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private Initializable replaceSceneContent(String fxml, String title) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        Parent root =  loader.load(in);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
