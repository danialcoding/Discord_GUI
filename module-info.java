module p {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens project.Discord.client.gui.fxml.menu;
    opens project.Discord.client.gui.fxml.menu.home_menu;
    opens project.Discord.client.gui.fxml.menu.home_menu.friends_menu;
    opens project.Discord.client.gui.fxml.menu.servers_menu;
    opens project.Discord.client.gui.fxml.profile;
    opens project.Discord.client.gui;
    opens project.Discord.client.gui.fxml.singup;
    opens project.Discord.client.gui.fxml.login;

}
