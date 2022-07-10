module p {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens project.Discord.client.gui.fxml.menu;
    opens project.Discord.client.gui.fxml.menu.home_menu;
    opens project.Discord.client.gui.fxml.menu.home_menu.chat;
    opens project.Discord.client.gui.fxml.menu.home_menu.friends_menu;
    opens project.Discord.client.gui.fxml.menu.home_menu.friends_menu.add_friend;
    opens project.Discord.client.gui.fxml.menu.home_menu.friends_menu.all_friends;
    opens project.Discord.client.gui.fxml.menu.home_menu.friends_menu.online_friends;
    opens project.Discord.client.gui.fxml.menu.home_menu.friends_menu.blocked;
    opens project.Discord.client.gui.fxml.menu.home_menu.friends_menu.pending;
    opens project.Discord.client.gui.fxml.menu.servers_menu;
    opens project.Discord.client.gui.fxml.profile;
    opens project.Discord.client.gui;
    opens project.Discord.client.gui.fxml.singup;
    opens project.Discord.client.gui.fxml.login;

}
