package project.Discord.client.gui.fxml.profile;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import project.Discord.client.GraphicalInterface;
import project.Discord.client.gui.RunGUI;
import project.Discord.client.gui.fxml.login.LoginController;
import project.Discord.client.gui.fxml.menu.MenuController;
import project.Discord.client.gui.fxml.menu.home_menu.HomeMenuController;
import project.Discord.client.gui.fxml.menu.home_menu.friends_menu.add_friend.AddFriendController;
import project.Discord.client.gui.fxml.profile.edit.EditController;
import project.Discord.client.gui.fxml.profile.edit_pass.EditPasswordController;
import project.Discord.server.entity.DiscordServer;
import project.Discord.server.entity.User;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProfileControler implements Initializable {

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

    public Text getUsername_text() {
        return username_text;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        edit_username_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,blackButtonHover);
        edit_username_button.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,blackButtonExitHover);

        edit_email_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,blackButtonHover);
        edit_email_button.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,blackButtonExitHover);

        edit_phone_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,blackButtonHover);
        edit_phone_button.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,blackButtonExitHover);

        edit_user_profile_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,blueButtonHover);
        edit_user_profile_button.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,blueButtonExitHover);

        change_password_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,blueButtonHover);
        change_password_button.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,blueButtonExitHover);

        logout_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,redButtonHover);
        logout_button.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,redButtonExitHover);

        esc_button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,redButtonHover);
        esc_button.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,redButtonExitHover);

        clickEditUserName();
        clickEditEmail();
        clickEditPhone();
        clickEditPass();
        logOut();
        clickESC();
        clickEditProfileButton();
    }

    public void setData(GraphicalInterface graphicalInterface) {
        this.graphicalInterface = graphicalInterface;
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
                ByteArrayInputStream bis = new ByteArrayInputStream(graphicalInterface.getUserPhoto(user.getUserName()));

                BufferedImage bImage2 = ImageIO.read(bis);

                ImageIO.write(bImage2, ".jpg",new File(String.valueOf(RunGUI.class.getResource("photo/user/" + 0))));
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            Image img = new Image(String.valueOf(RunGUI.class.getResource("photo/user/" + 0 + ".jpg")));

            profile_photo_circle.setFill(new ImagePattern(img));

            profile_photo_circle.setRadius(55);
        }
        else {
            InputStream stream = getClass().getResourceAsStream("/project/Discord/client/gui/icons/discord_icon.png");

            Image img = new Image(stream);

            ImagePattern imagePattern = new ImagePattern((img));

            profile_photo_circle.setFill(imagePattern);

            profile_photo_circle.setRadius(40);
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
        btn.setStyle("-fx-background-radius: 4; -fx-font-size: 18; -fx-font-weight: bold; -fx-background-color: #3b44a9; -fx-text-fill: #ffffff;");
    }

    public void blueButtonExitHover(Button btn) {
        btn.setStyle("-fx-background-radius: 4; -fx-font-size: 18; -fx-font-weight: bold; -fx-background-color: #5865f2; -fx-text-fill: #ffffff;");
    }

    public void redButtonHover(Button btn) {
        btn.setStyle("-fx-background-radius: 4; -fx-font-size: 18; -fx-font-weight: bold; -fx-background-color: #9a292b; -fx-text-fill: #ffffff;");
    }

    public void redButtonExitHover(Button btn) {
        btn.setStyle("-fx-background-radius: 4; -fx-font-size: 18; -fx-font-weight: bold; -fx-background-color: #d83c3e; -fx-text-fill: #ffffff;");
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


    public void clickEditUserName() {
        edit_username_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FXMLLoader fxmlLoader = new FXMLLoader(EditController.class.getResource("edit.fxml"));

                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                EditController ec = fxmlLoader.getController();

                ec.setData(graphicalInterface,edit_username_button.getId(), ProfileControler.this);

                Scene scene = new Scene(root);

                Stage stage = new Stage();

                stage.initModality(Modality.WINDOW_MODAL);

                stage.setResizable(false);

                stage.initStyle(StageStyle.UNDECORATED);

                stage.setScene(scene);

                stage.show();
            }
        });
    }

    public void clickEditEmail() {
        edit_email_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FXMLLoader fxmlLoader = new FXMLLoader(EditController.class.getResource("edit.fxml"));

                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                EditController ec = fxmlLoader.getController();

                ec.setData(graphicalInterface,edit_email_button.getId(), ProfileControler.this);

                Scene scene = new Scene(root);

                Stage stage = new Stage();

                stage.initModality(Modality.WINDOW_MODAL);

                stage.setResizable(false);

                stage.initStyle(StageStyle.UNDECORATED);

                stage.setScene(scene);

                stage.show();
            }
        });
    }

    public void clickEditPhone() {
        edit_phone_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FXMLLoader fxmlLoader = new FXMLLoader(EditController.class.getResource("edit.fxml"));

                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                EditController ec = fxmlLoader.getController();

                ec.setData(graphicalInterface,edit_phone_button.getId(), ProfileControler.this);

                Scene scene = new Scene(root);

                Stage stage = new Stage();

                stage.initModality(Modality.WINDOW_MODAL);

                stage.setResizable(false);

                stage.initStyle(StageStyle.UNDECORATED);

                stage.setScene(scene);

                stage.show();
            }
        });
    }

    public void clickEditPass() {
        change_password_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FXMLLoader fxmlLoader = new FXMLLoader(EditPasswordController.class.getResource("edit_password.fxml"));

                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                EditPasswordController epc = fxmlLoader.getController();

                epc.setData(graphicalInterface,edit_phone_button.getId(), ProfileControler.this);

                Scene scene = new Scene(root);

                Stage stage = new Stage();

                stage.initModality(Modality.WINDOW_MODAL);

                stage.setResizable(false);

                stage.initStyle(StageStyle.UNDECORATED);

                stage.setScene(scene);

                stage.show();
            }
        });
    }

    public void logOut() {
        logout_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                graphicalInterface.logOut();

                Parent root = null;

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login.fxml"));

                    root = fxmlLoader.load();

                    LoginController loginController = fxmlLoader.getController();

                    loginController.setData(graphicalInterface);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);

                stage.setScene(scene);

                stage.show();
            }
        });
    }

    public void clickESC() {
        esc_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                graphicalInterface.logOut();

                Parent root = null;

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("menu.fxml"));

                    root = fxmlLoader.load();

                    MenuController mc = fxmlLoader.getController();

                    mc.setData(graphicalInterface);

                    mc.setData(graphicalInterface);

                    ArrayList<DiscordServer> servers  = graphicalInterface.loadServers();

                    mc.setPageInformations(servers);

                    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

                    Scene scene = new Scene(root);

                    stage.setScene(scene);

                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void clickEditProfileButton() {
        edit_user_profile_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FileChooser fileChooser = new FileChooser();

                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("jpg Files","*.jpg"));

                File file = fileChooser.showOpenDialog(null);

                byte [] data = null;
                BufferedImage bImage = null;
                if(file != null) {
                    try {
                        bImage = ImageIO.read(file);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ImageIO.write(bImage, "jpg", bos );
                        data = bos.toByteArray();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                graphicalInterface.loadUser().setUserPhoto(data);

                graphicalInterface.loadUser().setHavePhoto(true);

                graphicalInterface.changePhoto(data);

                loadProfile();
            }
        });
    }


}