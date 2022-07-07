package project.Discord.client.gui.fxml.menu;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerItemController implements Initializable {

    @FXML
    private Circle server_image_circle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setData(byte[] imgByte,int index) {

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(imgByte);

            BufferedImage bImage2 = ImageIO.read(bis);

            ImageIO.write(bImage2, "jpg", new File("project/Discord/client/gui/photo/server/" + index + ".jpg"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Image img = new Image("project/Discord/client/gui/photo/server/" + index + ".jpg");

        server_image_circle.setFill(new ImagePattern(img));
    }

    public void setData(String address,int index) {

        InputStream stream = getClass().getResourceAsStream(address);

        Image img = new Image(stream);

        ImagePattern imagePattern = new ImagePattern((img));

        server_image_circle.setFill(imagePattern);
    }

}
