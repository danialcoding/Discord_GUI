package project.Discord.client.gui.fxml.profile.edit;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.Discord.client.GraphicalInterface;
import project.Discord.client.InputStatus;
import project.Discord.client.gui.fxml.profile.ProfileControler;
import project.Discord.networkPortocol.ObjectRequested;
import project.Discord.networkPortocol.Request;
import project.Discord.networkPortocol.RequestType;

import java.net.URL;
import java.util.ResourceBundle;

public class EditController implements Initializable {
    private GraphicalInterface graphicalInterface;

    private ProfileControler pc;

    @FXML
    private ImageView close_button;;

    @FXML
    private Text label_text;

    @FXML
    private Text topic_text;

    @FXML
    private Text description_text;

    @FXML
    private TextField change_textfield;

    @FXML
    private Button cancel_button;

    @FXML
    private Button done_button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hoverCancelButton();
        exitHoverCancelButton();
        hoverDoneButton();
        exitHoverDoneButton();
        closeStage();
        cancelStage();
    }

    public void setData(GraphicalInterface graphicalInterface, String btnID,ProfileControler pc) {
        this.graphicalInterface = graphicalInterface;

        this.pc = pc;

        switch (btnID) {
            case "edit_username_button" -> {
                label_text.setText("USERNAME");
                topic_text.setText("Change your username");
                description_text.setText("Enter a new username.");

                editUserName();
            }
            case "edit_email_button" -> {
                label_text.setText("EMAIL");
                topic_text.setText("Change your email");
                description_text.setText("Enter a new email.");

                editEmail();
            }
            case "edit_phone_button" -> {
                label_text.setText("PHONE NUMBER");
                topic_text.setText("Change your phone number");
                description_text.setText("Enter a new phone number.");

                editPhone();
            }
        }
    }

    public void closeStage() {
        close_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Stage stage = (Stage) close_button.getScene().getWindow();
                stage.close();
            }
        });
    }

    public void cancelStage() {
        cancel_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Stage stage = (Stage) cancel_button.getScene().getWindow();
                stage.close();
            }
        });
    }

    public void hoverCancelButton() {
        cancel_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                cancel_button.setUnderline(true);
            }
        });
    }

    public void exitHoverCancelButton() {
        cancel_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                cancel_button.setUnderline(false);
            }
        });
    }


    public void hoverDoneButton() {
        done_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                done_button.setStyle("-fx-background-radius: 4; -fx-font-size: 18; -fx-font-weight: bold; -fx-background-color: #3b44a9; -fx-text-fill: #ffffff;");

            }
        });
    }

    public void exitHoverDoneButton() {
        done_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                done_button.setStyle("-fx-background-radius: 4; -fx-font-size: 18; -fx-font-weight: bold; -fx-background-color: #5865f2; -fx-text-fill: #ffffff;");
            }
        });
    }

    public void editUserName() {
        done_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String userName = change_textfield.getText();

                if(userName.equals("")) {
                    label_text.setFill(Color.rgb(204,118,121));
                    label_text.setText("USERNAME - This field is required");
                    return;
                }
                else {
                    label_text.setText("USERNAME");
                    label_text.setFill(Color.rgb(154, 154, 154));
                }

                InputStatus is = graphicalInterface.userNameChecker(userName);


                if(is == InputStatus.FormatError) {
                    label_text.setText("USERNAME - Format invalid");
                    label_text.setFill(Color.rgb(204,118,121));
                    return;
                }
                else if(is == InputStatus.Duplicate) {
                    label_text.setText("USERNAME - Username is duplicate");
                    label_text.setFill(Color.rgb(204,118,121));
                    return;
                }
                else {
                    label_text.setText("USERNAME");
                    label_text.setFill(Color.rgb(154, 154, 154));
                }

                graphicalInterface.loadUser().setUserName(userName);

                graphicalInterface.changeUserName(userName);

                /*Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000),
                    actionEvent -> {
                        System.out.println("load prof...");
                        pc.loadProfile();
                    }
                ));
                timeline.setCycleCount(10);
                timeline.play();
                */

                pc.loadProfile();

                Stage stage = (Stage) done_button.getScene().getWindow();
                stage.close();
            }
        });
    }

    public void editEmail() {
        done_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String email = change_textfield.getText();

                if(email.equals("")) {
                    label_text.setFill(Color.rgb(204,118,121));
                    label_text.setText("EMAIL - This field is required");
                    return;
                }
                else {
                    label_text.setText("EMAIL");
                    label_text.setFill(Color.rgb(154, 154, 154));
                }

                InputStatus is = graphicalInterface.emailChecker(email);


                if(is == InputStatus.FormatError) {
                    label_text.setText("EMAIL - Format invalid");
                    label_text.setFill(Color.rgb(204,118,121));
                    return;
                }
                else if(is == InputStatus.Duplicate) {
                    label_text.setText("EMAIL - Username is duplicate");
                    label_text.setFill(Color.rgb(204,118,121));
                    return;
                }
                else {
                    label_text.setText("EMAIL");
                    label_text.setFill(Color.rgb(154, 154, 154));
                }

                graphicalInterface.loadUser().setEmail(email);

                graphicalInterface.changeEmail(email);

                pc.loadProfile();

                Stage stage = (Stage) done_button.getScene().getWindow();
                stage.close();
            }
        });
    }

    public void editPhone() {
        done_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String phoneNumber = change_textfield.getText();

                if(phoneNumber.equals("")) {
                    label_text.setFill(Color.rgb(204,118,121));
                    label_text.setText("PHONE NUMBER - This field is required");
                    return;
                }
                else {
                    label_text.setText("PHONE NUMBER");
                    label_text.setFill(Color.rgb(154, 154, 154));
                }

                InputStatus is = graphicalInterface.phoneChecker(phoneNumber);

                if(is == InputStatus.FormatError) {
                    label_text.setFill(Color.rgb(204,118,121));
                    label_text.setText("PHONE NUMBER - Format invalid");
                    return;
                }
                else {
                    label_text.setText("PHONE NUMBER");
                    label_text.setFill(Color.rgb(154, 154, 154));
                }

                graphicalInterface.loadUser().setPhoneNumber(phoneNumber);

                graphicalInterface.changePhoneNumber(phoneNumber);

                pc.loadProfile();

                Stage stage = (Stage) done_button.getScene().getWindow();
                stage.close();
            }
        });
    }

}
