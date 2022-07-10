package project.Discord.client.gui.fxml.menu.home_menu.friends_menu.blocked;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import project.Discord.client.GraphicInputStatus;
import project.Discord.client.GraphicalInterface;
import project.Discord.client.gui.fxml.menu.home_menu.HomeMenuController;
import project.Discord.server.entity.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BlockedFriendsController implements Initializable {

    private GraphicalInterface graphicalInterface;

    private HomeMenuController hmc;

    @FXML
    private Text topic_text;

    @FXML
    private VBox friends_menu_vbox;



    public void setdata(GraphicalInterface graphicalInterface,HomeMenuController hmc) {
        this.graphicalInterface = graphicalInterface;
        this.hmc = hmc;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void loadBlocks() {

        ArrayList<User> Friends = graphicalInterface.loadFriends();

        friends_menu_vbox.getChildren().clear();

        ArrayList<User> blocked = new ArrayList<>();

        for (int i = 0; i < Friends.size(); i++) {
            if(graphicalInterface.checkStatus(Friends.get(i).getUserName()) == GraphicInputStatus.Successful) {

                blocked.add(Friends.get(i));

                FXMLLoader fxmlLoader = new FXMLLoader();

                fxmlLoader.setLocation(BlockedFriendItemController.class.getResource("blocked_friend_item.fxml"));

                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BlockedFriendItemController ofic = fxmlLoader.getController();

                ofic.setData(graphicalInterface,Friends.get(i),i,this);

                if(i == 0) {
                    friends_menu_vbox.getChildren().clear();
                }

                friends_menu_vbox.getChildren().add(0,root);
            }
        }

        if(Friends.size() == 0) {
            topic_text.setText("BLOCKED");
        }
        else {
            topic_text.setText("BLOCKEDS - " + blocked.size());
        }
    }

    public void openPrivateMessage(User friend) {
        hmc.loadPrivateChat(friend);

        hmc.loadPrivateChats(graphicalInterface.loadPrivateChats(),graphicalInterface.loadUser());
    }
}