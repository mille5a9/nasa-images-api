package Controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import Services.PhotosCacheService;

public class NasaController implements INasaController {
    HttpClient client;
    private String key;

    public NasaController(String keyString) {
        client = HttpClient.newHttpClient();
        key = keyString;
    }

    // Get images from the past 10 earth days, max 3 per day
    public String GetTenDays(int maxImagesPerDay) {
        String responseString = "";
        
        for (int i = 10; i > 0; i--) {

            // get 10 days ago 
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -i);

            // format date for URI
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String dateArg = "" + year + "-" + month + "-" + day;

            // check cache for this day
            PhotosCacheService cache = new PhotosCacheService();
            String cachedData = cache.Read(dateArg);
            if (cachedData != null) {
                responseString += cachedData;
                responseString += "\n";
                continue;
            }


            String uriString = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date=" 
                + dateArg + "&camera=NAVCAM&api_key=" + key;

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriString))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .GET()
                .build();

            try {
                HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

                JSONObject obj = new JSONObject(response.body());
                JSONArray photos = obj.getJSONArray("photos");
                String nextImage = "";

                // get relevant info from the first three images if they exist
                for (int j = 0; j < 3; j++) {
                    if (photos.isNull(j)) break;
                    obj = photos.getJSONObject(j);
                    nextImage += "\n" + obj.getString("img_src") + ",";
                }

                responseString += dateArg + " {" + nextImage + "\n}\n";

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return responseString;
    }
}
