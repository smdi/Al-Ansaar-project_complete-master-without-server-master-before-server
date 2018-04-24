package al_muntaqimcrescent2018.com.al_ansar;

/**
 * Created by Imran on 31-01-2018.
 */

public class HomeInitialiser {

    private String uri;
    private String heading;
    private String description;
    private String date;
    private String link;


    public HomeInitialiser(){

    }

    public HomeInitialiser(String uri, String heading, String description, String date, String link) {
        this.uri = uri;
        this.heading = heading;
        this.description = description;
        this.date = date;
        this.link = link;
    }

    public String getUri() {
        return uri;
    }

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getLink() {
        return link;
    }
}
