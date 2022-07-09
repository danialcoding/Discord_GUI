package project.Discord.client.gui.fxml.menu.home_menu.chat;

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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    private GraphicalInterface graphicalInterface;

    @FXML
    private VBox chat_page_vbox;

    @FXML
    private ImageView send_file_image;

    @FXML
    private TextField message_textfield;

    @FXML
    private VBox chat_vbox;

    public void setData(GraphicalInterface graphicalInterface) {
        this.graphicalInterface = graphicalInterface;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        enterToSendNewMessage();
    }

    public void loadMessages(String userName) {
        ArrayList<Message> messages = graphicalInterface.createPrivateChat(userName);

        System.out.println(messages);

        chat_vbox.getChildren().clear();

        if(messages == null) {
            return;
        }

        for (int i = 0; i < messages.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(MessageController.class.getResource("message.fxml"));

            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MessageController mc = fxmlLoader.getController();

            mc.setData(graphicalInterface,messages.get(i));

            if(i == 0) {
                chat_vbox.getChildren().clear();
            }

            chat_page_vbox.getChildren().add(0,root);
        }
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

        if(gis == GraphicInputStatus.NotSuccessful) {
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

        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(MessageController.class.getResource("message.fxml"));

        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MessageController mc = fxmlLoader.getController();

        Message msg = new Message(graphicalInterface.loadUser().getUserName(),text);

        mc.setData(graphicalInterface,msg);

        chat_page_vbox.getChildren().add(0,root);
    }
}
