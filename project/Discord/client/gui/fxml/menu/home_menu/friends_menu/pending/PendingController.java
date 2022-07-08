package project.Discord.client.gui.fxml.menu.home_menu.friends_menu.pending;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import project.Discord.client.GraphicalInterface;
import project.Discord.server.entity.FriendRequest;
import project.Discord.server.entity.User;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PendingController implements Initializable {
    private GraphicalInterface graphicalInterface;

    @FXML
    private VBox friends_menu_vbox;

    @FXML
    private Text topic_text;


    public void setData(GraphicalInterface graphicalInterface) {
        this.graphicalInterface = graphicalInterface;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void loadPendings() {
        User user = graphicalInterface.loadUser();

        ArrayList<FriendRequest> friendRequests = graphicalInterface.loadFriendRequest();

        friends_menu_vbox.getChildren().clear();

        if(friendRequests.size() == 0) {
            topic_text.setText("PENDING");
        }
        else {
            topic_text.setText("PENDING - " + friendRequests.size());
        }

        for (int i = 0; i < friendRequests.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(PendingItemcontroller.class.getResource("pending_item.fxml"));

            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            PendingItemcontroller pic = fxmlLoader.getController();

            pic.setData(graphicalInterface,friendRequests.get(i).getSender(),friendRequests.get(i).getGetter(),user,i,this);

            if(i == 0) {
                friends_menu_vbox.getChildren().clear();
            }

            friends_menu_vbox.getChildren().add(0,root);
        }
    }

    public void updateFriendRequest(String id,int index) {
        switch (id) {
            case "accept_circle" -> {
                graphicalInterface.pendingFriendRequest("accept",index);
                loadPendings();
            }
            case "cancel_circle" -> {
                graphicalInterface.pendingFriendRequest("reject",index);
                loadPendings();
            }
        }
    }
}
