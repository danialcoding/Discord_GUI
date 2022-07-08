package project.Discord.client.gui.fxml.menu.home_menu.chat;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import project.Discord.server.entity.User;

import java.io.InputStream;

public class ChatTopBarController {

    @FXML
    private Circle user_status;

    @FXML
    private ImageView inbox_image;

    @FXML
    private Text username_text;

    public void setData(User friend) {
        username_text.setText(friend.getUserName());

        String address = "";

        switch (friend.getStatus()) {
            case ONLINE -> address = "/project/Discord/client/gui/icons/online.png";
            case OFFLINE -> address = "/project/Discord/client/gui/icons/offline.png";
            case IDLE -> address = "/project/Discord/client/gui/icons/idle.png";
            case INVISIBLE -> address = "/project/Discord/client/gui/icons/offline.png";
            case DO_NOT_DISTURB -> address = "/project/Discord/client/gui/icons/do_not_disturb.png";
            case NULL -> {
                if(friend.isActive()) {
                    address = "/project/Discord/client/gui/icons/online.png";
                }
                else {
                    address = "/project/Discord/client/gui/icons/offline.png";
                }
            }
        }

        InputStream stream = getClass().getResourceAsStream(address);

        Image img = new Image(stream);

        ImagePattern imagePattern = new ImagePattern((img));

        user_status.setFill(imagePattern);
    }
}
