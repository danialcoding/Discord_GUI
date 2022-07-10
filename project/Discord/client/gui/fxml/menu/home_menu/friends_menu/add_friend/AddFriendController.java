package project.Discord.client.gui.fxml.menu.home_menu.friends_menu.add_friend;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project.Discord.client.GraphicInputStatus;
import project.Discord.client.GraphicalInterface;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddFriendController implements Initializable {
    private GraphicalInterface graphicalInterface;

    @FXML
    private Text error_text;

    @FXML
    private HBox hbox;

    @FXML
    private VBox friends_menu_vbox;

    @FXML
    private TextField search_username_textfield;

    @FXML
    private Button send_friend_request_button;

    public void setData(GraphicalInterface graphicalInterface) {
        this.graphicalInterface = graphicalInterface;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        enterHoverSend_friend_request_button();

        exitHoverSend_friend_request_button();

        sendFriendRequest();
    }

    public void enterHoverSend_friend_request_button() {
        send_friend_request_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                send_friend_request_button.setStyle("-fx-background-radius: 3; -fx-font-size: 18; -fx-font-weight: bold; -fx-background-color: #404bb9; -fx-text-fill: #ffffff;");
            }
        });
    }

    public void exitHoverSend_friend_request_button() {
        send_friend_request_button.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                send_friend_request_button.setStyle("-fx-background-radius: 3; -fx-font-size: 18; -fx-font-weight: bold; -fx-background-color: #5865f2; -fx-text-fill: #ffffff;");
            }
        });
    }

    public void sendFriendRequest() {
        send_friend_request_button.addEventHandler(MouseEvent.MOUSE_CLICKED,new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
               String userName = search_username_textfield.getText();

               GraphicInputStatus graphicInputStatus = graphicalInterface.addFriend(userName);

               if(graphicInputStatus == GraphicInputStatus.UserNameFormatError) {
                   error_text.setText("Username invalid format error.");
                   error_text.setFill(Color.rgb(185,110,113));
               }
               else if(graphicInputStatus == GraphicInputStatus.NotSuccessful) {
                   try {
                       FXMLLoader fxmlLoader = new FXMLLoader(InvalidUsernameController.class.getResource("invalid_username.fxml"));

                       Parent root = fxmlLoader.load();

                       InvalidUsernameController iuc = fxmlLoader.getController();

                       iuc.setData(AddFriendController.this);

                       Scene scene = new Scene(root);

                       Stage stage = new Stage();

                       stage.initModality(Modality.WINDOW_MODAL);

                       stage.setResizable(false);

                       stage.initStyle(StageStyle.UNDECORATED);

                       stage.setScene(scene);

                       stage.show();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
               else {
                   error_text.setText("Success! Your friend request to " + userName + " was sent");
                   error_text.setFill(Color.rgb(67,168,100));
               }
            }
        });
    }

    public void setError() {
        error_text.setText("Hm, didn't work. Double check that the capitalization, spelling, any spaces, and numbers are correct.");
        error_text.setFill(Color.rgb(185,110,113));
    }
}