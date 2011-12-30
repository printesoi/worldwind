package worldwind;

public class Application {
    private static String name;
    private static String author;
    private static String version;
    private static String licence;
    private static int year;
    
    public Application(String fName, String fVersion, String fAuthor, String fLicence, int fYear) {
        name = fName;
        author = fAuthor;
        version = fVersion;
        year = fYear;
        licence = fLicence;
    }

    public String getName() {
        return name;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getVersion() {
        return version;
    }

    public String getLicence() {
        return licence;
    }
    
    public int getYear() {
        return year;
    }
}
