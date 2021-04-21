import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import Controllers.NasaController;
import Services.PhotosCacheService;

public class Program {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Call Program with a Command argument: \"query\", \"clear\", \"clean\", or \"help\".");
            return;
        }

        // get api key from key.properties
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("src/key.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // init cache for clear/clean
        PhotosCacheService cache = new PhotosCacheService();

        // init controller for query
        var nasa = new NasaController(prop.getProperty("APIKEY"));
        String output = "";

        if (args[0].equals("query")) {
            output = nasa.GetTenDays(3);
        }
        else if (args[0].equals("clear")) {
            cache.Clear();
            output = "Deleted all data from local cache.";
        }
        else if (args[0].equals("clean")) {
            cache.Clean();
            output = "Deleted all data from local cache that was older than ten days.";
        }
        else if (args[0].equals("help")) {
            output = "Available commands:\n"
                + "clean - Removes all data from the cache that is more than ten days old, as it will not be useful to keep.\n"
                + "clear - Removes all data from the cache.\n"
                + "query - Returns JSON response from querying the NASA Open API for up to three images from Curiosity's NAVCAM each of the past ten days\n";
        }

        System.out.println(output);
    }
}