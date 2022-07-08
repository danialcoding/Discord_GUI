package project.Discord.client.gui.fxml.menu.home_menu.friends_menu.all_friends;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import project.Discord.client.GraphicalInterface;
import project.Discord.client.gui.fxml.menu.home_menu.HomeMenuController;
import project.Discord.server.entity.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AllFriendsController implements Initializable {

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

    public void loadAllfriends() {
        //User user = graphicalInterface.loadUser();

        ArrayList<User> Friends = graphicalInterface.loadFriends();

        friends_menu_vbox.getChildren().clear();

        if(Friends.size() == 0) {
            topic_text.setText("ALL FRIENDS");
        }
        else {
            topic_text.setText("ALL FRIENDS - " + Friends.size());
        }

        for (int i = 0; i < Friends.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(FriendItemController.class.getResource("friend_item.fxml"));

            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FriendItemController fic = fxmlLoader.getController();

            fic.setData(graphicalInterface,Friends.get(i),i,this);

            if(i == 0) {
                friends_menu_vbox.getChildren().clear();
            }

            friends_menu_vbox.getChildren().add(0,root);
        }
    }

    public void openPrivateMessage(User friend) {
        hmc.loadPrivateChat(friend);
    }
}