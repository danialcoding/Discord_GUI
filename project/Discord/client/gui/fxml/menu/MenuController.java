package project.Discord.client.gui.fxml.menu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import project.Discord.client.GraphicalInterface;
import project.Discord.client.gui.fxml.menu.home_menu.HomeMenuController;
import project.Discord.server.entity.DiscordServer;
import project.Discord.server.entity.PrivateChat;
import project.Discord.server.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    private GraphicalInterface graphicalInterface;

    @FXML
    private Circle home_button;

    @FXML
    private VBox servers_vbox;

    @FXML
    private HBox menu_load_hbox;

    public void setData(GraphicalInterface graphicalInterface) {
        this.graphicalInterface = graphicalInterface;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InputStream stream = getClass().getResourceAsStream("/project/Discord/client/gui/icons/discord_icon.png");

        Image img = new Image(stream);

        ImagePattern imagePattern = new ImagePattern((img));

        home_button.setFill(imagePattern);


    }

    public void setPageInformations(ArrayList<DiscordServer> servers) {
        int i = 0;
        for (; i < servers.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(MenuController.class.getResource("server_item.fxml"));

            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ServerItemController sic = fxmlLoader.getController();

            sic.setData(servers.get(i).getServerPhoto(),i);

            servers_vbox.getChildren().add(root);

        }

        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(MenuController.class.getResource("plus_item.fxml"));

        Pane pane = null;
        try {
            pane = fxmlLoader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        PlusItemController pic = fxmlLoader.getController();

        pic.setData("/project/Discord/client/gui/icons/plus_g.png");

        servers_vbox.getChildren().add(pane);

        ///menu loader

        fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(HomeMenuController.class.getResource("home_menu.fxml"));

        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HomeMenuController hmc = fxmlLoader.getController();

        hmc.setData(graphicalInterface);

        ArrayList<PrivateChat> privateChats = graphicalInterface.loadPrivateChats();

        User user = graphicalInterface.loadUser();

        hmc.setPageInformation(privateChats,user);

        hmc.setUserData(user);

        menu_load_hbox.getChildren().add(root);
    }
}