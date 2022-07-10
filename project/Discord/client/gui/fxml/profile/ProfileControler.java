package project.Discord.client.gui.fxml.profile;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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

public class ProfileControler implements Initializable {

    private Stage prevStage;

    private GraphicalInterface graphicalInterface;


    @FXML
    private Button esc_button;

    @FXML
    private Circle profile_photo_circle;

    @FXML
    private Circle status_circle;

    @FXML
    private Text username_text;

    @FXML
    private Text profile_username_text;

    @FXML
    private Button edit_user_profile_button;

    @FXML
    private Button edit_username_button;

    @FXML
    private Text email_text;

    @FXML
    private Button edit_email_button;

    @FXML
    private Text phone_text;

    @FXML
    private Button edit_phone_button;

    @FXML
    private Button change_password_button;

    @FXML
    private Button logout_button;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setData(Stage prevStage, GraphicalInterface graphicalInterface) {
        this.graphicalInterface = graphicalInterface;
        this.prevStage = prevStage;
    }

    public void loadProfile() {
        User user = graphicalInterface.loadUser();

        profile_username_text.setText(user.getUserName());

        username_text.setText(user.getUserName());

        email_text.setText(user.getEmail());

        if(user.getPhoneNumber().equals("")) {
            phone_text.setText("You haven't added a phone number yet.");

            edit_phone_button.setText("Add");
        }
        else {
            phone_text.setText(user.getPhoneNumber());

            edit_phone_button.setText("Edit");
        }

        setPhoto(user);
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

            case ONLINE -> address =  "/project/Discord/client/gui/icons/online.png";
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

    public void blueButtonHover(Button btn) {
        btn.setStyle("-fx-background-radius: 4; -fx-font-size: 18; -fx-font-weight: bold; -fx-background-color: #3b44a9; -fx-alignment: right; -fx-text-fill: #ffffff;");
    }

    public void blueButtonExitHover(Button btn) {
        btn.setStyle("-fx-background-radius: 4; -fx-font-size: 18; -fx-font-weight: bold; -fx-background-color: #5865f2; -fx-alignment: right; -fx-text-fill: #ffffff;");
    }

    public void redButtonHover(Button btn) {
        btn.setStyle("-fx-background-radius: 4; -fx-font-size: 18; -fx-font-weight: bold; -fx-background-color: #9a292b; -fx-alignment: right; -fx-text-fill: #ffffff;");
    }

    public void redButtonExitHover(Button btn) {
        btn.setStyle("-fx-background-radius: 4; -fx-font-size: 18; -fx-font-weight: bold; -fx-background-color: #d83c3e; -fx-alignment: right; -fx-text-fill: #ffffff;");
    }

    public void blackButtonHover(Button btn) {
        btn.setStyle("-fx-background-color: #616670; -fx-font-size: 20; -fx-font-weight: bold; -fx-background-radius: 5; -fx-text-fill: #ffffff;");
    }

    public void blackButtonExitHover(Button btn) {
        btn.setStyle("-fx-background-color: #4f545c; -fx-font-size: 20; -fx-font-weight: bold; -fx-background-radius: 5; -fx-text-fill: #ffffff;");
    }



    EventHandler<MouseEvent> blueButtonHover = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Button btn = (Button) mouseEvent.getSource();

            blueButtonHover(btn);
        }
    };

    EventHandler<MouseEvent> blueButtonExitHover = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Button btn = (Button) mouseEvent.getSource();

            blueButtonExitHover(btn);
        }
    };

    EventHandler<MouseEvent> redButtonHover = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Button btn = (Button) mouseEvent.getSource();

            redButtonHover(btn);
        }
    };

    EventHandler<MouseEvent> redButtonExitHover = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Button btn = (Button) mouseEvent.getSource();

            redButtonExitHover(btn);
        }
    };

    EventHandler<MouseEvent> blackButtonHover = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Button btn = (Button) mouseEvent.getSource();

            blackButtonHover(btn);
        }
    };

    EventHandler<MouseEvent> blackButtonExitHover = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Button btn = (Button) mouseEvent.getSource();

            blackButtonExitHover(btn);
        }
    };


    //clicks


    


}