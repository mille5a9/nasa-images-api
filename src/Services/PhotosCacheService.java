package Services;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class PhotosCacheService implements ICacheService<String, String> {
    public final String CacheFile = "photos.cache";
    
    // removes all entries from >10 days ago
    public void Clean() {

        // get 10 days ago for comparison
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -10);
        Date tenDaysAgo = cal.getTime();

        var formatter = new SimpleDateFormat("yyyy-MM-dd");

        // place to store info that stays
        String newCacheContents = "";

        try (var cacheReader = new BufferedReader(new FileReader(CacheFile))) {

            // find matching date, return next line
            String line = cacheReader.readLine();
            while (line != null) {
                String[] content = line.split("\\s+");
                Date entry = formatter.parse(content[0]);
                if (entry.after(tenDaysAgo)) {
                    newCacheContents += content[0] + "\t" + content[1] + "\n";
                }

                // load up next date for conditional
                line = cacheReader.readLine();
            }
        } catch (Exception ex) { // IOException or ParseException
            ex.printStackTrace();
        }

        try (var cacheWriter = new FileWriter(CacheFile)) {

            // rewrite the saved entries
            cacheWriter.write(newCacheContents);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // removes all entries
    public void Clear() {

        try (var cacheWriter = new FileWriter(CacheFile)) {

            // overwrite the file
            cacheWriter.write("");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // adds previously uncached day's reponse, skips writing if the response is already there
    public void Write(String day, String response) {
        makeCacheFile();

        ArrayList<String> isCached = Read(day);
        if (isCached.isEmpty() == false) {
            for (String cached : isCached) {
                if (cached.equals(response)) return;
            }
        }

        try (FileWriter cacheWriter = new FileWriter(CacheFile, true)) {
            cacheWriter.append(day + "\t" + response + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // returns list of images for given day, empty list if no images
    public ArrayList<String> Read(String day) {
        makeCacheFile();

        try (var cacheReader = new BufferedReader(new FileReader(CacheFile))) {
            ArrayList<String> output = new ArrayList<String>();

            // find matching date, return next line
            String line = cacheReader.readLine();
            while (line != null) {
                String[] content = line.split("\\s+");
                // if (line.equals(day)) return cacheReader.readLine();
                // else cacheReader.readLine(); // cycle past unrelated cache entry
                if (content[0].equals(day)) {
                    output.add(content[1]);
                }

                // next line
                line = cacheReader.readLine();
            }

            return output;

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return new ArrayList<String>();
    }

    // make cache file if it isn't there
    private void makeCacheFile() {
        File cacheLocation = new File(CacheFile);

        // make cache if it isn't there
        try {
            cacheLocation.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
