package project.Discord.client.gui.fxml.menu.home_menu;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import project.Discord.client.GraphicalInterface;
import project.Discord.client.gui.fxml.menu.PlusItemController;
import project.Discord.client.gui.fxml.menu.home_menu.friends_menu.FriendsMenuController;
import project.Discord.server.entity.PrivateChat;
import project.Discord.server.entity.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeMenuController implements Initializable {
    private GraphicalInterface graphicalInterface;

    @FXML
    private Button friends_menu_button;

    @FXML
    private HBox top_bar;

    @FXML
    private HBox friends_menu_hbox;

    @FXML
    private TextField search_textfield;

    @FXML
    private VBox direct_messages_vbox;

    @FXML
    private Circle user_profile_circle;

    @FXML
    private Circle user_status_circle;

    @FXML
    private Text username_text;

    public void setData(GraphicalInterface graphicalInterface) {
        this.graphicalInterface = graphicalInterface;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        friends_menu_button.addEventHandler(MouseEvent.MOUSE_CLICKED,hover);

        friends_menu_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);

        exitFriendsMenuButton();

        openFriendsMenu();
    }

    public void openFriendsMenu() {
        friends_menu_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FXMLLoader fxmlLoader = new FXMLLoader();

                fxmlLoader.setLocation(FriendsMenuController.class.getResource("friends_menu.fxml"));

                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                FriendsMenuController fmc = fxmlLoader.getController();

                fmc.setData(graphicalInterface);

                top_bar.getChildren().set(1,root);
            }
        });
    }

    EventHandler<Event> hover = new EventHandler<Event>() {
        @Override
        public void handle(Event event) {
            friends_menu_button.setStyle("-fx-background-color: #42464d; -fx-alignment: Top-Left; -fx-background-radius: 5; -fx-text-fill: #96989d; -fx-font-size: 22; -fx-font-weight: bold; -fx-graphic-text-gap: 15;");
        }
    };

    public void exitFriendsMenuButton() {
        friends_menu_button.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                friends_menu_button.setStyle("-fx-background-color: transparent; -fx-alignment: Top-Left; -fx-background-radius: 5; -fx-text-fill: #96989d; -fx-font-size: 22; -fx-font-weight: bold; -fx-graphic-text-gap: 15;");
            }
        });
    }

    public void setPageInformation(ArrayList<PrivateChat> privateChats,User user) {
        for (int i = 0; i < privateChats.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(PrivateChatItemController.class.getResource("friend_item.fxml"));

            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            PrivateChatItemController pcic = fxmlLoader.getController();

            if(privateChats.get(i).getUser1().getUserName().equals(user.getUserName())) {
                pcic.setData(privateChats.get(i).getUser2());
            }
            else {
                pcic.setData(privateChats.get(i).getUser1());
            }

            direct_messages_vbox.getChildren().add(root);
        }
    }

    public void setUserData(User user) {

        username_text.setText(user.getUserName());

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

            user_profile_circle.setFill(new ImagePattern(img));
        }
        else {
            InputStream stream = getClass().getResourceAsStream("/project/Discord/client/gui/icons/discord_icon.png");

            Image img = new Image(stream);

            ImagePattern imagePattern = new ImagePattern((img));

            user_profile_circle.setFill(imagePattern);

            user_profile_circle.setRadius(20);
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

        user_status_circle.setFill(imagePattern);
    }
}