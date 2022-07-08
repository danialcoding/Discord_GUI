package project.Discord.client.gui.fxml.menu.home_menu.chat;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import project.Discord.client.GraphicalInterface;
import project.Discord.server.entity.Message;
import project.Discord.server.entity.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class MessageController implements Initializable {

    private GraphicalInterface graphicalInterface;

    @FXML
    private HBox main_hbox;

    @FXML
    private Circle profile_photo_circle;

    @FXML
    private Text username_text;

    @FXML
    private Text date_text;

    @FXML
    private Text message_text;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setData(GraphicalInterface graphicalInterface, Message message) {
        username_text.setText(message.getSender());

        date_text.setText(message.getDateTime());

        message_text.setText(message.getContent());

        setPhoto(graphicalInterface.loadSelectedUser(message.getSender()));
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
    }
}
