package project.Discord.client.gui.fxml.menu.home_menu.friends_menu.pending;

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
import project.Discord.client.gui.RunGUI;
import project.Discord.server.entity.User;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class PendingItemcontroller implements Initializable {
    private GraphicalInterface graphicalInterface;

    private PendingController pc;

    private int index;

    @FXML
    private HBox main_hbox;

    @FXML
    private Circle accept_under_circle;

    @FXML
    private Circle profile_photo_circle;

    @FXML
    private Circle status_circle;

    @FXML
    private Text username_text;

    @FXML
    private Text request_status_text;

    @FXML
    private Circle cancel_circle;

    @FXML
    private Circle accept_circle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hoverCancel();
        exitHoverCancel();
        hoverAccept();
        exitHoverAccept();
        clickAccept();
        clickCancel();
    }

    public void hoverCancel() {
        cancel_circle.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                InputStream stream = getClass().getResourceAsStream("/project/Discord/client/gui/icons/cancel_hover.png");

                Image img = new Image(stream);

                ImagePattern imagePattern = new ImagePattern((img));

                cancel_circle.setFill(imagePattern);
            }
        });
    }

    public void exitHoverCancel() {
        cancel_circle.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                InputStream stream = getClass().getResourceAsStream("/project/Discord/client/gui/icons/cancel.png");

                Image img = new Image(stream);

                ImagePattern imagePattern = new ImagePattern((img));

                cancel_circle.setFill(imagePattern);
            }
        });
    }

    public void hoverAccept() {
        accept_circle.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                InputStream stream = getClass().getResourceAsStream("/project/Discord/client/gui/icons/accept_hover.png");

                Image img = new Image(stream);

                ImagePattern imagePattern = new ImagePattern((img));

                accept_circle.setFill(imagePattern);
            }
        });
    }

    public void exitHoverAccept() {
        accept_circle.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                InputStream stream = getClass().getResourceAsStream("/project/Discord/client/gui/icons/accept.png");

                Image img = new Image(stream);

                ImagePattern imagePattern = new ImagePattern((img));

                accept_circle.setFill(imagePattern);
            }
        });
    }

    public void setData(GraphicalInterface graphicalInterface, User sender, User getter, User user,int index,PendingController pc) {

        this.pc = pc;

        this.graphicalInterface = graphicalInterface;

        this.index = index;

        if(getter.getUserName().equals(user.getUserName())) {
            username_text.setText(sender.getUserName());

            setPhoto(sender);

            request_status_text.setText("Incoming Friend Request");

            InputStream stream = getClass().getResourceAsStream("/project/Discord/client/gui/icons/cancel.png");

            Image img = new Image(stream);

            ImagePattern imagePattern = new ImagePattern((img));

            cancel_circle.setFill(imagePattern);

            InputStream stream2 = getClass().getResourceAsStream("/project/Discord/client/gui/icons/accept.png");

            Image img2 = new Image(stream2);

            ImagePattern imagePattern2 = new ImagePattern((img2));

            accept_circle.setFill(imagePattern2);
        }
        else {
            username_text.setText(getter.getUserName());

            setPhoto(getter);

            request_status_text.setText("Outgoing Friend Request");

            InputStream stream = getClass().getResourceAsStream("/project/Discord/client/gui/icons/cancel.png");

            Image img = new Image(stream);

            ImagePattern imagePattern = new ImagePattern((img));

            cancel_circle.setFill(imagePattern);

            accept_circle.setVisible(false);

            accept_under_circle.setVisible(false);
        }
    }

    public void setPhoto(User user) {
        if(user.getHavePhoto()) {

            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(user.getUserPhoto());

                BufferedImage bImage2 = ImageIO.read(bis);

                ImageIO.write(bImage2, "jpg", new File(String.valueOf(RunGUI.class.getResource("photo/user/" + 0))));
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            Image img = new Image(String.valueOf(RunGUI.class.getResource("photo/user/" + 0 + ".jpg")));

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

    public void clickAccept() {
        accept_circle.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pc.updateFriendRequest(accept_circle.getId(),index);
            }
        });
    }

    public void clickCancel() {
        cancel_circle.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pc.updateFriendRequest(accept_circle.getId(),index);
            }
        });
    }


}
