package project.Discord.client.clientException;

public class InputFileAddressException extends Exception {

    public InputFileAddressException() {
        super("Invalid input.");
    }

    public InputFileAddressException(String message) {
        super(message);
    }
}
