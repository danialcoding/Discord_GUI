package project.Discord.client.gui.fxml.login;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.Discord.client.GraphicalInterface;
import project.Discord.client.Updator;
import project.Discord.client.gui.fxml.menu.MenuController;
import project.Discord.client.gui.fxml.singup.SingUpController;
import project.Discord.server.entity.DiscordServer;
import project.Discord.server.entity.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private GraphicalInterface graphicalInterface;

    @FXML
    private Pane pane;

    @FXML
    private TextField username_text_field;

    @FXML
    private TextField password_text_field;

    @FXML
    private Button login_button;

    @FXML
    private Text register_text;

    @FXML
    private Text username_text;

    @FXML
    private Text password_text;


    public void setData(GraphicalInterface graphicalInterface) {
        this.graphicalInterface = graphicalInterface;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        switchToRegister();

        enteredRegister_text();

        exitedRegister_text();

        enteredLogin_button();

        exitedLogin_button();

        login();
    }

    public void switchToRegister() {
        register_text.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {

                Parent root = null;
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(SingUpController.class.getResource("singup.fxml"));

                    root = fxmlLoader.load();

                    SingUpController singUpController = fxmlLoader.getController();

                    singUpController.setData(graphicalInterface);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);

                stage.setScene(scene);

                stage.show();
            }
        });
    }

    public void switchToMenu() {
        login_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {

                Parent root = null;
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("menu.fxml"));

                    root = fxmlLoader.load();

                    MenuController menuController = fxmlLoader.getController();

                    menuController.setData(graphicalInterface);

                    ArrayList<DiscordServer> servers  = graphicalInterface.loadServers();

                    menuController.setPageInformations(servers);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);

                stage.setScene(scene);

                stage.show();
            }
        });
    }

    public void enteredRegister_text() {
        register_text.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(
                    MouseEvent mouseEvent) {
                register_text.setUnderline(true);
            }
        });
    }

    public void exitedRegister_text() {
        register_text.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                register_text.setUnderline(false);
            }
        });
    }

    public void enteredLogin_button() {
        login_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                login_button.setStyle("-fx-background-color: #3e49b9; -fx-font-size: 18; -fx-font-weight: bold; -fx-border-radius: 5; -fx-alignment: center; -fx-text-fill: #ffffff");
            }
        });
    }

    public void exitedLogin_button() {
        login_button.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                login_button.setStyle("-fx-background-color: #5865f2; -fx-font-size: 18; -fx-font-weight: bold; -fx-border-radius: 5; -fx-alignment: center; -fx-text-fill: #ffffff");
            }
        });
    }

    public void login() {
        login_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String userName = username_text_field.getText();

                userName = userName.toLowerCase();

                String password = password_text_field.getText();

                if (!checkEmptyLogin(userName, password)) {
                    return;
                }

                Boolean check = graphicalInterface.login(userName,password);

                if(check) {
                    switchToMenu();
                }
                else {
                    username_text.setFill(Color.rgb(204,118,121));
                    username_text.setText("USERNAME - Username or password is invalid");

                    password_text.setFill(Color.rgb(204,118,121));
                    password_text.setText("PASSWORD - Username or password is invalid");
                }
            }
        });
    }

    public Boolean checkEmptyLogin(String userName,String password) {
        boolean checkEmpty = true;

        if(userName.equals("")) {
            username_text.setFill(Color.rgb(204,118,121));
            username_text.setText("USERNAME - This field is required");
            checkEmpty = false;
        }
        else {
            username_text.setFill(Color.rgb(154, 154, 154));
            username_text.setText("USERNAME");
        }

        if(password.equals("")) {
            password_text.setFill(Color.rgb(204,118,121));
            password_text.setText("PASSWORD - This field is required");
            checkEmpty = false;
        }
        else {
            password_text.setFill(Color.rgb(154, 154, 154));
            password_text.setText("PASSWORD");
        }

        return checkEmpty;
    }
}
