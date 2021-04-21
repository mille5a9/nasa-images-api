package Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import Services.PhotosCacheService;

public class PhotosCacheServiceTests {
    private PhotosCacheService cache = new PhotosCacheService();

    @Test
    public void Write_NormalDayAndResponse_RecordInCache() {

        // act
        cache.Write("day", "response");

        // assert
        try (var reader = new BufferedReader(new FileReader(cache.CacheFile))) {
            String written = reader.readLine();
            String[] content = written.split("\\s+");
            assertEquals(content[0], "day");
            assertEquals(content[1], "response");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cache.Clear();
    }

    @Test
    public void Read_TwoImagesFromOneDay_ReturnsBothImages() {

        // arrange
        cache.Write("day", "response1");
        cache.Write("day", "response2");

        // act
        ArrayList<String> data = cache.Read("day");

        // assert
        assertEquals(data.get(0), "response1");
        assertEquals(data.get(1), "response2");
        
        cache.Clear();
    }

    @Test
    public void Clean_OneOldImageOneNewImage_RemovesOldImageFromCache() {

        // arrange
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String dateArg = "" + year + "-" + month + "-" + day;
        cache.Write("2000-1-1", "oldDay");
        cache.Write(dateArg, "newDay");

        // act
        cache.Clean();

        // assert
        ArrayList<String> data = cache.Read("2000-1-1");
        assertTrue(data.isEmpty());

        data = cache.Read(dateArg);
        assertEquals(data.get(0), "newDay");

        cache.Clear();
    }
}
