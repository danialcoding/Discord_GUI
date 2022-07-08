package project.Discord.client.gui.fxml.singup;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.Discord.client.GraphicInputStatus;
import project.Discord.client.GraphicalInterface;
import project.Discord.client.gui.fxml.login.LoginController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SingUpController implements Initializable {

    private GraphicalInterface graphicalInterface;

    @FXML
    private Pane pane;

    @FXML
    private TextField email_text_field;

    @FXML
    private TextField username_text_field;

    @FXML
    private TextField password_text_field;

    @FXML
    private Text email_text;

    @FXML
    private Text username_text;

    @FXML
    private Text password_text;

    @FXML
    private Button singup_button;

    @FXML
    private Text login_text;

    @FXML
    private CheckBox agree_checkbox;

    public void setData(GraphicalInterface graphicalInterface) {
        this.graphicalInterface = graphicalInterface;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        enterLogin_text();

        exitLogin_text();

        enterSingup_button();

        exitSingup_button();

        singUp();

        switchToLoginLoginText();
    }

    EventHandler<Event> switchToLogin = new EventHandler<Event>() {
        @Override
        public void handle(Event event) {
            Parent root = null;

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login.fxml"));

                root = fxmlLoader.load();

                LoginController loginController = fxmlLoader.getController();

                loginController.setData(graphicalInterface);

            } catch (IOException e) {
                e.printStackTrace();
            }

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();
        }
    };

    public void switchToLoginLoginText() {
        login_text.addEventHandler(MouseEvent.MOUSE_CLICKED,switchToLogin);

    }

    public void switchToLoginSingUpButton() {
        singup_button.addEventHandler(MouseEvent.MOUSE_CLICKED,switchToLogin);
    }

    public void enterLogin_text() {
        login_text.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                login_text.setUnderline(true);
            }
        });
    }

    public void exitLogin_text() {
        login_text.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                login_text.setUnderline(false);
            }
        });
    }

    public void enterSingup_button() {
        singup_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                singup_button.setStyle("-fx-background-color: #3e49b9; -fx-font-size: 18; -fx-font-weight: bold; -fx-border-radius: 5; -fx-alignment: center; -fx-text-fill: #ffffff");

            }
        });
    }
    public void exitSingup_button() {
        singup_button.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                singup_button.setStyle("-fx-background-color: #5865f2; -fx-font-size: 18; -fx-font-weight: bold; -fx-border-radius: 5; -fx-alignment: center; -fx-text-fill: #ffffff");
            }
        });
    }


    public void singUp() {
        singup_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String userName = username_text_field.getText();

                String email = email_text_field.getText();

                String password = password_text_field.getText();

                if(!checkEmptyLogin(email,userName,password)) {
                    return;
                }

                if(!agree_checkbox.isSelected()) {
                   agree_checkbox.setTextFill(Color.rgb(204,118,121));
                   return;
                }
                else {
                    agree_checkbox.setTextFill(Color.rgb(154, 154, 154));
                }

                GraphicInputStatus gis = graphicalInterface.singUp(email,userName,password);


                if(gis == GraphicInputStatus.EmailFormatError) {
                    email_text.setText("EMAIL - Format invalid");
                    email_text.setFill(Color.rgb(204,118,121));
                }
                else if(gis == GraphicInputStatus.EmailDuplicate) {
                    email_text.setText("EMAIL - Email is duplicate");
                    email_text.setFill(Color.rgb(204,118,121));
                }
                else {
                    email_text.setText("EMAIL");
                    email_text.setFill(Color.rgb(154, 154, 154));
                }

                if(gis == GraphicInputStatus.UserNameFormatError) {
                    username_text.setText("USERNAME - Format invalid");
                    username_text.setFill(Color.rgb(204,118,121));
                }
                else if(gis == GraphicInputStatus.UserNameDuplicate) {
                    username_text.setText("USERNAME - Username is duplicate");
                    username_text.setFill(Color.rgb(204,118,121));
                }
                else {
                    username_text.setText("USERNAME");
                    username_text.setFill(Color.rgb(154, 154, 154));
                }

                if(gis == GraphicInputStatus.PasswordFormatError) {
                    password_text.setText("PASSWORD - Format invalid");
                    password_text.setFill(Color.rgb(204,118,121));
                }
                else {
                    password_text.setText("PASSWORD");
                    password_text.setFill(Color.rgb(154, 154, 154));
                }

                if(gis == GraphicInputStatus.Successful) {
                    switchToLoginSingUpButton();
                }
            }
        });
    }

    public Boolean checkEmptyLogin(String email,String userName,String password) {
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

        if(email.equals("")) {
            email_text.setFill(Color.rgb(204,118,121));
            email_text.setText("EMAIL - This field is required");
            checkEmpty = false;
        }
        else {
            email_text.setFill(Color.rgb(154, 154, 154));
            email_text.setText("EMAIL");
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
