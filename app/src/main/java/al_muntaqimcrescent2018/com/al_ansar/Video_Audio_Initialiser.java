package al_muntaqimcrescent2018.com.al_ansar;

/**
 * Created by Imran on 08-02-2018.
 */

public class Video_Audio_Initialiser {

    String uri;
    String description;
    String date;

    public Video_Audio_Initialiser() {
    }

    public Video_Audio_Initialiser(String uri, String description, String date) {
        this.uri = uri;
        this.description = description;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getUri() {
        return uri;
    }

    public String getDescription() {
        return description;
    }
}
