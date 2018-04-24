package al_muntaqimcrescent2018.com.al_ansar;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.sdsmdg.tastytoast.TastyToast;

public class Contact_Creator extends AppCompatActivity {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dbreference;
    private EditText contactName ,profession ,mail;
    private LottieAnimationView lottieAnimationView;
    private RelativeLayout lotrel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__creator);


        initialise();
      firbaseinitialise();
    }

    private void initialise() {
        contactName = (EditText) findViewById(R.id.editContactName);
        profession  = (EditText) findViewById(R.id.editProfession);
        mail        = (EditText) findViewById(R.id.editMail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.push,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.push :

               getLotte();
                TastyToast.makeText(getApplicationContext(),"pushing",Toast.LENGTH_SHORT,TastyToast.INFO).show();
                AndroidSystempusher();


                break;

        }

        return true;
    }

    private void AndroidSystempusher() {

        final String getContact = ""+contactName.getText();
        final String getMail    = ""+mail.getText();
        final String getProf    = ""+profession.getText();

      if(getContact.length() >3 && getMail.contains("@gmail.com")&& getProf.length()>3) {
          DatabaseReference databaseReference = dbreference;
          ContactInitialiser contactInitialiser = new ContactInitialiser(getContact.replaceAll("\\s+"," "), getProf.replaceAll("\\s+"," "), getMail);
          databaseReference.push().setValue(contactInitialiser).addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {

                  try {
                      cancelAnim();
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {

                  TastyToast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT,TastyToast.ERROR).show();
                 cancelAnim();
              }
          });
          finish();

      }
      else {


          TastyToast.makeText(getApplicationContext(), "Enter Valid details", Toast.LENGTH_SHORT,TastyToast.INFO).show();
         cancelAnim();
      }
    }
    private void firbaseinitialise() {
        String fireDb = corrector( "Contact-list");
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference().child(fireDb);

    }

    private String corrector(String master) {

        String fin = "";

        String mod = "" ,mod1 = "",mod2 = "",mod3 = "",mod4="",mod5 = "";



        if(master.contains("."))
        {
            mod = ""+master.replace(".","dot") ;
        }
        else{
            mod = master;
        }
        if(mod.contains("$"))
        {


            mod1 = ""+mod.replace("$","dollar");
        }
        else{
            mod1 = mod;
        }
        if(mod1.contains("["))
        {

            mod2 = ""+mod1.replace("[","lsb");
        }
        else{
            mod2 = mod1;
        }
        if(mod2.contains("]"))
        {

            mod3 = ""+mod2.replace("]","rsb");
        }
        else{
            mod3 = mod2;
        }
        if(mod3.contains("#"))
        {

            mod4 = ""+mod3.replace("#","hash");
        }
        else{
            mod4 = mod3;
        }
        if(mod4.contains("/"))
        {

            mod5 = ""+mod4.replace("/","fs");

            fin = mod5;
        }
        else{
            mod5 = mod4;

            fin = mod5;

        }


        System.out.println(""+fin);

        return   fin;
    }
    public void getLotte() {

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view);
        lottieAnimationView.setAnimation("preloader.json");
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);
        lotrel = (RelativeLayout) findViewById(R.id.lotteRel);
        lotrel.setVisibility(View.VISIBLE);
    }
    private void cancelAnim() {

        lottieAnimationView.setVisibility(View.GONE);
        lottieAnimationView.cancelAnimation();
        lotrel.setVisibility(View.GONE);
    }

}
