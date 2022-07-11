package project.Discord.client.gui.fxml.menu.home_menu;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.Discord.client.GraphicalInterface;
import project.Discord.client.gui.RunGUI;
import project.Discord.client.gui.fxml.menu.home_menu.chat.ChatController;
import project.Discord.client.gui.fxml.profile.ProfileControler;
import project.Discord.client.gui.fxml.menu.home_menu.chat.ChatTopBarController;
import project.Discord.client.gui.fxml.menu.home_menu.friends_menu.FriendsMenuController;
import project.Discord.client.gui.fxml.menu.home_menu.friends_menu.add_friend.AddFriendController;
import project.Discord.client.gui.fxml.menu.home_menu.friends_menu.all_friends.AllFriendsController;
import project.Discord.client.gui.fxml.menu.home_menu.friends_menu.blocked.BlockedFriendsController;
import project.Discord.client.gui.fxml.menu.home_menu.friends_menu.online_friends.OnlineFriendsController;
import project.Discord.client.gui.fxml.menu.home_menu.friends_menu.pending.PendingController;
import project.Discord.server.entity.DiscordServer;
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
    private TextField search_textfield;

    @FXML
    private VBox direct_messages_vbox;

    @FXML
    private Circle user_profile_circle;

    @FXML
    private Circle user_status_circle;

    @FXML
    private Text username_text;

    @FXML
    private Pane friends_menu_pane;

    @FXML
    private ImageView setting_image;


    public void setData(GraphicalInterface graphicalInterface) {
        this.graphicalInterface = graphicalInterface;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        friends_menu_button.addEventHandler(MouseEvent.MOUSE_CLICKED,hover);

        friends_menu_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,hover);

        exitFriendsMenuButton();

        openFriendsMenu();

        openUserSetting();
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

                fmc.setParentController(HomeMenuController.this);

                top_bar.getChildren().set(1,root);

                Pane pane = new Pane();

                pane.setStyle("-fx-background-color: #36393f; -fx-pref-height: 780;-fx-pref-width: 1090;");

                friends_menu_pane.getChildren().set(0,pane);
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

    public void loadPrivateChats(ArrayList<PrivateChat> privateChats,User user) {
        direct_messages_vbox.getChildren().clear();

        for (int i = 0; i < privateChats.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(PrivateChatItemController.class.getResource("private_chat_item.fxml"));

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

    public void loadFriendsMenuVbox(String id) {

        switch (id) {
            case "online_button" -> {
                FXMLLoader fxmlLoader = new FXMLLoader();

                fxmlLoader.setLocation(OnlineFriendsController.class.getResource("online_friends.fxml"));

                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                OnlineFriendsController ofc = fxmlLoader.getController();

                ofc.setdata(graphicalInterface,this);

                ofc.loadAllfriends();

                friends_menu_pane.getChildren().set(0,root);
            }
            case "all_button" -> {
                FXMLLoader fxmlLoader = new FXMLLoader();

                fxmlLoader.setLocation(AllFriendsController.class.getResource("all_friends.fxml"));

                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                AllFriendsController afc = fxmlLoader.getController();

                afc.setdata(graphicalInterface,this);

                afc.loadAllfriends();

                friends_menu_pane.getChildren().set(0,root);
            }
            case "pending_button" -> {
                FXMLLoader fxmlLoader = new FXMLLoader();

                fxmlLoader.setLocation(PendingController.class.getResource("pending.fxml"));

                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                PendingController pc = fxmlLoader.getController();

                pc.setData(graphicalInterface);

                pc.loadPendings();

                friends_menu_pane.getChildren().set(0,root);
            }
            case "blocked_button" -> {
                FXMLLoader fxmlLoader = new FXMLLoader();

                fxmlLoader.setLocation(BlockedFriendsController.class.getResource("block_friends.fxml"));

                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BlockedFriendsController bfc = fxmlLoader.getController();

                bfc.setdata(graphicalInterface,this);

                bfc.loadBlocks();

                friends_menu_pane.getChildren().set(0,root);
            }
            case "add_friend_button" -> {
                FXMLLoader fxmlLoader = new FXMLLoader();

                fxmlLoader.setLocation(AddFriendController.class.getResource("add_friend.fxml"));

                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                AddFriendController afc = fxmlLoader.getController();

                afc.setData(graphicalInterface);

                friends_menu_pane.getChildren().set(0,root);
            }
        }
    }


    public void loadPrivateChat(User friend) {
        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(ChatTopBarController.class.getResource("chat_top_bar.fxml"));

        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ChatTopBarController ctb = fxmlLoader.getController();

        ctb.setData(friend);

        top_bar.getChildren().set(1,root);



        FXMLLoader fxmlLoader1 = new FXMLLoader();

        fxmlLoader1.setLocation(ChatController.class.getResource("chat.fxml"));

        Parent root1 = null;
        try {
            root1 = fxmlLoader1.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ChatController cc = fxmlLoader1.getController();

        cc.setData(graphicalInterface,graphicalInterface.loadUser());

        cc.loadMessages(friend.getUserName());

        friends_menu_pane.getChildren().set(0,root1);
    }

    public void openUserSetting() {
        setting_image.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Parent root = null;
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(ProfileControler.class.getResource("profile.fxml"));

                    root = fxmlLoader.load();

                    ProfileControler profileControler = fxmlLoader.getController();

                    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

                    Scene scene = new Scene(root);

                    stage.setScene(scene);

                    profileControler.setData(stage,graphicalInterface);

                    profileControler.loadProfile();

                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}