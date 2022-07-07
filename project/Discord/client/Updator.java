package project.Discord.client;

public class Updator implements Runnable {

    private GraphicalInterface graphicalInterface;

    public Updator(GraphicalInterface graphicalInterface) {
        this.graphicalInterface = graphicalInterface;
    }

    @Override
    public void run() {
        while (true) {
            graphicalInterface.update();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
