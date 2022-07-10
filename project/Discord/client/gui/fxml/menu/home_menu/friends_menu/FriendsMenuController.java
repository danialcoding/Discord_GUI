package project.Discord.client.gui.fxml.menu.home_menu.friends_menu;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import project.Discord.client.GraphicalInterface;
import project.Discord.client.gui.fxml.menu.home_menu.HomeMenuController;

import java.net.URL;
import java.util.ResourceBundle;

public class FriendsMenuController implements Initializable {

    private GraphicalInterface graphicalInterface;

    private HomeMenuController hmc;

    @FXML
    private Button online_button;

    @FXML
    private Button all_button;

    @FXML
    private Button pending_button;

    @FXML
    private Button blocked_button;

    @FXML
    private Button add_friend_button;

    @FXML
    private ImageView inbox_image;


    public void setData(GraphicalInterface graphicalInterface) {
        this.graphicalInterface = graphicalInterface;

    }

    public void setParentController(HomeMenuController hmc) {
        this.hmc = hmc;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        online_button.addEventHandler(MouseEvent.MOUSE_CLICKED,click);
        all_button.addEventHandler(MouseEvent.MOUSE_CLICKED,click);
        pending_button.addEventHandler(MouseEvent.MOUSE_CLICKED,click);
        blocked_button.addEventHandler(MouseEvent.MOUSE_CLICKED,click);
        add_friend_button.addEventHandler(MouseEvent.MOUSE_CLICKED,clickAddFriendButton);

        online_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
        all_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
        pending_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
        blocked_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
    }

    public void getClick(Button btn) {
        String id = btn.getId();

        switch (id) {
            case "online_button" -> {
                normal(all_button);
                normal(pending_button);
                normal(blocked_button);
                exitAddFriend(add_friend_button);

                all_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
                pending_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
                blocked_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
                add_friend_button.removeEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,enterHoverAddFriendButton);
                add_friend_button.removeEventHandler(MouseEvent.MOUSE_EXITED_TARGET,exitHoverAddFriendButton);
            }
            case "all_button" -> {
                normal(online_button);
                normal(pending_button);
                normal(blocked_button);
                exitAddFriend(add_friend_button);

                online_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
                pending_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
                blocked_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
                add_friend_button.removeEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,enterHoverAddFriendButton);
                add_friend_button.removeEventHandler(MouseEvent.MOUSE_EXITED_TARGET,exitHoverAddFriendButton);
            }
            case "pending_button" -> {
                normal(online_button);
                normal(all_button);
                normal(blocked_button);
                exitAddFriend(add_friend_button);

                online_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
                all_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
                blocked_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
                add_friend_button.removeEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,enterHoverAddFriendButton);
                add_friend_button.removeEventHandler(MouseEvent.MOUSE_EXITED_TARGET,exitHoverAddFriendButton);
            }
            case "blocked_button" -> {
                normal(online_button);
                normal(all_button);
                normal(pending_button);
                exitAddFriend(add_friend_button);

                online_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
                all_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
                pending_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
                add_friend_button.removeEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,enterHoverAddFriendButton);
                add_friend_button.removeEventHandler(MouseEvent.MOUSE_EXITED_TARGET,exitHoverAddFriendButton);
            }
            case "add_friend_button" -> {
                normal(online_button);
                normal(all_button);
                normal(pending_button);
                normal(blocked_button);

                online_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
                all_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
                pending_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
                blocked_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
            }
        }
        hmc.loadFriendsMenuVbox(id);
    }

    public void hover(Button btn) {
        btn.setStyle("-fx-background-color: #42464d; -fx-alignment: Top-Left; -fx-background-radius: 5; -fx-text-fill: #ffffff; -fx-font-size: 18; -fx-font-weight: bold; -fx-graphic-text-gap: 15;");
    }

    public void normal(Button btn) {
        btn.setStyle("-fx-background-color: #36393f; -fx-alignment: Top-Left; -fx-background-radius: 5; -fx-text-fill: #ffffff; -fx-font-size: 18; -fx-font-weight: bold; -fx-graphic-text-gap: 15;");
    }

    public void clickAddFriend(Button btn) {
        btn.setStyle("-fx-background-color: #36393f; -fx-alignment: Top-Left; -fx-background-radius: 5; -fx-text-fill: #3ba152; -fx-font-size: 18; -fx-font-weight: bold; -fx-graphic-text-gap: 15;");
    }

    public void exitAddFriend(Button btn) {
        btn.setStyle("-fx-background-color: #3ba55d; -fx-alignment: Top-Left; -fx-background-radius: 5; -fx-text-fill: #ffffff; -fx-font-size: 18; -fx-font-weight: bold; -fx-graphic-text-gap: 15;");
    }

    public void enterhoverAddFriend(Button btn) {
        btn.setStyle("-fx-background-color: #42464d; -fx-alignment: Top-Left; -fx-background-radius: 5; -fx-text-fill: #3ba152; -fx-font-size: 18; -fx-font-weight: bold; -fx-graphic-text-gap: 15;");
    }

    public void exitHoverAddFriend(Button btn) {
        btn.setStyle("-fx-background-color: #36393f; -fx-alignment: Top-Left; -fx-background-radius: 5; -fx-text-fill: #3ba55d; -fx-font-size: 18; -fx-font-weight: bold; -fx-graphic-text-gap: 15;");
    }

    EventHandler<MouseEvent> click = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Button btn = (Button) mouseEvent.getSource();

            hover(btn);
            getClick(btn);
            btn.removeEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);
            btn.removeEventHandler(MouseEvent.MOUSE_EXITED_TARGET,exitHover);
        }
    };

    EventHandler<MouseEvent> hover = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Button btn = (Button) mouseEvent.getSource();

            hover(btn);
            btn.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,exitHover);
        }
    };

    EventHandler<MouseEvent> exitHover = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Button btn = (Button) mouseEvent.getSource();
            normal(btn);
        }
    };

    EventHandler<MouseEvent> clickAddFriendButton = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            clickAddFriend(add_friend_button);
            getClick(add_friend_button);
            add_friend_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,enterHoverAddFriendButton);
            add_friend_button.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,exitHoverAddFriendButton);
        }
    };

    EventHandler<MouseEvent> enterHoverAddFriendButton = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            enterhoverAddFriend(add_friend_button);
        }
    };

    EventHandler<MouseEvent> exitHoverAddFriendButton = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            exitHoverAddFriend(add_friend_button);
        }
    };


}
