package project.Discord.client.gui.fxml.profile.edit_pass;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import project.Discord.server.entity.User;

import java.net.URL;
import java.util.ResourceBundle;


public class EditPasswordController implements Initializable {
    private GraphicalInterface graphicalInterface;

    private ProfileControler pc;

    @FXML
    private ImageView close_button;

    @FXML
    private Text topic_text;

    @FXML
    private Text description_text;

    @FXML
    private Text label_text;

    @FXML
    private TextField change_textfield;

    @FXML
    private Text old_pass_label_text;

    @FXML
    private TextField old_pass_textfield;

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
        editpassword();
    }

    public void setData(GraphicalInterface graphicalInterface, String btnID,ProfileControler pc) {
        this.graphicalInterface = graphicalInterface;

        this.pc = pc;
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

    public void editpassword() {
        done_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String password = change_textfield.getText();

                String oldPassword = old_pass_textfield.getText();

                if(password.equals("")) {
                    label_text.setFill(Color.rgb(204,118,121));
                    label_text.setText("PASSWORD - This field is required");
                    return;
                }
                else {
                    label_text.setText("PASSWORD");
                    label_text.setFill(Color.rgb(154, 154, 154));
                }

                if(oldPassword.equals("")) {
                    old_pass_label_text.setFill(Color.rgb(204,118,121));
                    old_pass_label_text.setText("OLD PASSWORD - This field is required");
                    return;
                }
                else {
                    old_pass_label_text.setText("OLD PASSWORD");
                    old_pass_label_text.setFill(Color.rgb(154, 154, 154));
                }


                InputStatus is = graphicalInterface.passwordChecker(password);


                if(is == InputStatus.FormatError) {
                    label_text.setText("PASSWORD - Format invalid");
                    label_text.setFill(Color.rgb(204,118,121));
                    return;
                }
                else {
                    label_text.setText("PASSWORD");
                    label_text.setFill(Color.rgb(154, 154, 154));
                }

                is = graphicalInterface.changePassword(password,oldPassword);

                if(is == InputStatus.NotSuccessful) {
                    old_pass_label_text.setText("OLD PASSWORD - This field is incorrect");
                    old_pass_label_text.setFill(Color.rgb(204,118,121));
                    return;
                }
                else {
                    old_pass_label_text.setText("OLD PASSWORD");
                    old_pass_label_text.setFill(Color.rgb(154, 154, 154));
                }

                graphicalInterface.loadUser().setPassWord(password);

                pc.loadProfile();

                Stage stage = (Stage) done_button.getScene().getWindow();
                stage.close();
            }
        });
    }

}
