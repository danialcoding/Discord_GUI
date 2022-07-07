package project.Discord.client.gui.fxml.menu.home_menu;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import project.Discord.server.entity.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class PrivateChatItemController implements Initializable {

    @FXML
    private HBox main_hbox;

    @FXML
    private Circle profile_photo_circle;

    @FXML
    private Circle status_circle;

    @FXML
    private Text username_text;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        main_hbox.addEventHandler(MouseEvent.MOUSE_ENTERED,hoverAndClicked);

        main_hbox.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hoverAndClicked);

        exitMainHBox();
    }

    EventHandler<Event> hoverAndClicked = new EventHandler<Event>() {
        @Override
        public void handle(Event event) {
            main_hbox.setStyle("-fx-background-color: #42464d; -fx-alignment: Top-Left; -fx-background-radius: 5; -fx-text-fill: #96989d; -fx-font-size: 22; -fx-font-weight: bold; -fx-graphic-text-gap: 15;");
        }
    };

    public void exitMainHBox() {
        main_hbox.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                main_hbox.setStyle("-fx-background-color: transparent; -fx-alignment: Top-Left; -fx-background-radius: 5; -fx-text-fill: #96989d; -fx-font-size: 22; -fx-font-weight: bold; -fx-graphic-text-gap: 15;");
            }
        });
    }

    public void setData(User getter) {

        username_text.setText(getter.getUserName());

        if(getter.getHavePhoto()) {

            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(getter.getUserPhoto());

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

        switch (getter.getStatus()) {
            case ONLINE -> address = "/project/Discord/client/gui/icons/online.png";
            case OFFLINE -> address = "/project/Discord/client/gui/icons/offline.png";
            case IDLE -> address = "/project/Discord/client/gui/icons/idle.png";
            case INVISIBLE -> address = "/project/Discord/client/gui/icons/offline.png";
            case DO_NOT_DISTURB -> address = "/project/Discord/client/gui/icons/do_not_disturb.png";
            case NULL -> {
                if(getter.isActive()) {
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
}
