package project.Discord.client.gui.fxml.menu.home_menu.friends_menu.all_friends;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import project.Discord.client.GraphicalInterface;
import project.Discord.server.entity.User;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class FriendItemController implements Initializable {

    private AllFriendsController afc;

    private GraphicalInterface graphicalInterface;

    private int index;

    private User friend;

    @FXML
    private HBox main_hbox;

    @FXML
    private Circle profile_photo_circle;

    @FXML
    private Circle status_circle;

    @FXML
    private Text username_text;

    @FXML
    private Text request_status_text;

    @FXML
    private Circle message_circle;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clickMessage();
    }


    public void setData(GraphicalInterface graphicalInterface, User friend, int index,AllFriendsController pc) {

        this.afc = pc;

        this.friend = friend;

        this.graphicalInterface = graphicalInterface;

        this.index = index;

        username_text.setText(friend.getUserName());

        setPhoto(friend);

        request_status_text.setText("Incoming Friend Request");

        InputStream stream = getClass().getResourceAsStream("/project/Discord/client/gui/icons/message.png");

        Image img = new Image(stream);

        ImagePattern imagePattern = new ImagePattern((img));

        message_circle.setFill(imagePattern);

    }

    public void setPhoto(User user) {
        if(user.getHavePhoto()) {

            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(user.getUserPhoto());

                BufferedImage bImage2 = ImageIO.read(bis);

                ImageIO.write(bImage2, "jpg", new File("project/Discord/client/gui/photo/user/" + 0 + ".jpg"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            Image img = new Image("project/Discord/client/gui/photo/user/" + 0 + ".jpg");

            profile_photo_circle.setFill(new ImagePattern(img));
        }
        else {
            InputStream stream = getClass().getResourceAsStream("/project/Discord/client/gui/icons/discord_icon.png");

            Image img = new Image(stream);

            ImagePattern imagePattern = new ImagePattern((img));

            profile_photo_circle.setFill(imagePattern);

            profile_photo_circle.setRadius(20);
        }

        String address = "";

        switch (user.getStatus()) {
            case ONLINE -> address = "/project/Discord/client/gui/icons/online.png";
            case OFFLINE -> address = "/project/Discord/client/gui/icons/offline.png";
            case IDLE -> address = "/project/Discord/client/gui/icons/idle.png";
            case INVISIBLE -> address = "/project/Discord/client/gui/icons/offline.png";
            case DO_NOT_DISTURB -> address = "/project/Discord/client/gui/icons/do_not_disturb.png";
            case NULL -> {
                if(user.isActive()) {
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

        status_circle.setFill(imagePattern);
    }

    public void clickMessage() {
        message_circle.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                afc.openPrivateMessage(friend);
            }
        });
    }


}
