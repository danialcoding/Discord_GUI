package project.Discord.client;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientApplication {

    private Client client;

    public static void main(String[] args) {
            ExecutorService executorService = Executors.newCachedThreadPool();

            executorService.execute(new Client(8000,"localhost"));
    }

    public Client start() {
        client = new Client(8000,"localhost");

        return client;
    }
}
