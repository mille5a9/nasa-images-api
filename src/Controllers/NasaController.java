package Controllers;

import java.net.http.HttpClient;

public class NasaController implements INasaController {
    HttpClient client;
    private String key;

    public NasaController(String keyString) {
        client = HttpClient.newHttpClient();
        key = keyString;
    }

    // Get images from the past 10 earth days, max 3 per day
    public String GetTenDays() {
        return "";
    }
}
