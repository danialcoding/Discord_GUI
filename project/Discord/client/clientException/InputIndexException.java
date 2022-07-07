package project.Discord.client.clientException;

public class InputIndexException extends Exception {

    public InputIndexException() {
        super("Invalid input.");
    }

    public InputIndexException(String message) {
        super(message);
    }
}