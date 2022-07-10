package project.Discord.client.gui.fxml.menu.home_menu.chat;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.Discord.client.GraphicInputStatus;
import project.Discord.client.GraphicalInterface;
import project.Discord.server.entity.Message;
import project.Discord.server.entity.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    private GraphicalInterface graphicalInterface;

    private ArrayList<Message> messages;

    private User user;

    @FXML
    private VBox chat_page_vbox;

    @FXML
    private ImageView send_file_image;

    @FXML
    private TextField message_textfield;

    @FXML
    private VBox chat_vbox;

    public void setData(GraphicalInterface graphicalInterface, User user) {
        this.graphicalInterface = graphicalInterface;
        this.user = user;
        if(messages == null) {
            this.messages = new ArrayList<>();
        }
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        enterToSendNewMessage();
    }

    public void loadMessages(String userName) {
        ArrayList<Message> messages = graphicalInterface.createPrivateChat(userName);

        this.messages = messages;

        chat_vbox.getChildren().clear();
        if(messages != null) {
            for (int i = messages.size()-1; i >= 0; i--) {

                FXMLLoader fxmlLoader = new FXMLLoader();

                fxmlLoader.setLocation(MessageController.class.getResource("message.fxml"));

                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MessageController mc = fxmlLoader.getController();

                mc.setData(graphicalInterface.loadSelectedUser(messages.get(i).getSender()),messages.get(i));

                if(i == 0) {
                    chat_vbox.getChildren().clear();
                }

                chat_page_vbox.getChildren().add(0,root);
            }
        }

        MessageGetter msgGetter = new MessageGetter(graphicalInterface,this,messages,graphicalInterface.getResponseHandler());

        Thread thread = new Thread(msgGetter);

        thread.start();
    }

    public void enterToSendNewMessage() {
        message_textfield.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER) {
                    String text = message_textfield.getText();
                    message_textfield.clear();
                    if(!text.equals("")) {
                        sendMessage(text);
                    }
                }
            }
        });
    }

    public void sendMessage(String text) {
        GraphicInputStatus gis = graphicalInterface.sendMessageToList(text);

        Message newMSG = new Message(user.getUserName(),text);

        if(messages == null) {
            this.messages = new ArrayList<>();
        }

        //messages.add(newMSG);

        graphicalInterface.getMessages().add(newMSG);

        if (gis == GraphicInputStatus.NotSuccessful) {
            try {
                Parent root = FXMLLoader.load(BlockedErrorController.class.getResource("blocked_error.fxml"));

                Scene scene = new Scene(root);

                Stage stage = new Stage();

                stage.initModality(Modality.WINDOW_MODAL);

                stage.setScene(scene);

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        graphicalInterface.sendMessageToFriend(text);

        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(MessageController.class.getResource("message.fxml"));

        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MessageController mc = fxmlLoader.getController();

        Message msg = new Message(user.getUserName(),text);

        mc.setData(user,msg);

        chat_page_vbox.getChildren().add(messages.size()-1,root);
    }

    public void updateMessage() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chat_vbox.getChildren().clear();

                //messages = graphicalInterface.getMessages();

                if(messages != null) {
                    for (int i = messages.size()-1; i >= 0; i--) {

                        FXMLLoader fxmlLoader = new FXMLLoader();

                        fxmlLoader.setLocation(MessageController.class.getResource("message.fxml"));

                        Parent root = null;
                        try {
                            root = fxmlLoader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        MessageController mc = fxmlLoader.getController();

                        mc.setData(graphicalInterface.loadSelectedUser(messages.get(i).getSender()),messages.get(i));

                        if(i == 0) {
                            chat_vbox.getChildren().clear();
                        }

                        chat_page_vbox.getChildren().add(0,root);
                    }
                }
            }
        });
        /*messages = graphicalInterface.getFriendMessages();

        chat_vbox.getChildren().clear();

        if(messages != null) {
            for (int i = messages.size()-1; i >= 0; i--) {

                FXMLLoader fxmlLoader = new FXMLLoader();

                fxmlLoader.setLocation(MessageController.class.getResource("message.fxml"));

                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MessageController mc = fxmlLoader.getController();

                mc.setData(graphicalInterface.loadSelectedUser(messages.get(i).getSender()),messages.get(i));

                if(i == 0) {
                    chat_vbox.getChildren().clear();
                }

                chat_page_vbox.getChildren().add(0,root);
            }
        }*/
    }
}
