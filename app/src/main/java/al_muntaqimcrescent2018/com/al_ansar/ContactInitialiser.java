package al_muntaqimcrescent2018.com.al_ansar;

/**
 * Created by Imran on 06-02-2018.
 */

public class ContactInitialiser {

    private String name;
    private String profession;
    private String mail;

    public ContactInitialiser()
    {

    }

    public ContactInitialiser(String name ,String profession ,String mail) {
        this.name = name;
        this.profession = profession;
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public String getProfession() {
        return profession;
    }


    public String getMail() {
        return mail;
    }
}
