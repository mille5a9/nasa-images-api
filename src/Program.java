import Services.PhotosCacheService;

public class Program {
    public static void main(String[] args) {
        System.out.println("Running main...");
        System.out.println(args.length);

        PhotosCacheService cache = new PhotosCacheService();
        cache.Write("2021-3-16", "fake response blah blah blah");
        cache.Write("2021-4-16", "fake response blah blah blah");
        cache.Clean();
    }
}