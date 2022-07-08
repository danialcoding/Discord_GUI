package project.Discord.client.gui.fxml.menu.home_menu.friends_menu.add_friend;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class InvalidUsernameController implements Initializable {
    @FXML
    private Button close_button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeError();
    }

    public void closeError() {
        close_button.addEventHandler(MouseEvent.MOUSE_CLICKED,new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Stage stage = (Stage) close_button.getScene().getWindow();
                stage.close();
            }
        });
    }


}
