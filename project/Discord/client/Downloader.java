package project.Discord.client;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Downloader implements Runnable {
    private URL url;

    private String addressForSave;

    public Downloader(URL url, String addressForSave) {
        this.url = url;
        this.addressForSave = addressForSave;
    }

    public void run() {
        try {
            saveFileFromUrl(addressForSave, String.valueOf(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFileFromUrl(String fileName, String fileUrl) throws IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;

        try {
            in = new BufferedInputStream(new URL(fileUrl).openStream());
            fout = new FileOutputStream(fileName);
            byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if ( in != null)
                in.close();
            if (fout != null)
                fout.close();
        }
    }
}
