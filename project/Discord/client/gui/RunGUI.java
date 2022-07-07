package project.Discord.client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project.Discord.client.Client;
import project.Discord.client.ClientApplication;
import project.Discord.client.gui.fxml.login.LoginController;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunGUI extends Application {

    ClientApplication clientApplication;

    @Override
    public void start(Stage stage) throws Exception {
        clientApplication = new ClientApplication();

        Client client = clientApplication.start();

        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.execute(client);

        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login.fxml"));

        Parent root =  fxmlLoader.load();

        LoginController loginController = fxmlLoader.getController();

        loginController.setData(client.getGraphicalInterface());

        Scene newScene = new Scene(root);

        stage.setScene(newScene);

        stage.setTitle("Discord");

        stage.show();

    }

   public void startApp() {
        launch();
    }
}
    /*Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxmFiles/homeMenu.fxml")));

    Scene scene = new Scene(root);

    stage.setScene(scene);

    stage.show();*/