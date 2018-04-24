package al_muntaqimcrescent2018.com.al_ansar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.PrivilegedAction;
import java.util.Calendar;

public class chooser extends AppCompatActivity {


    private ImageButton img;
    private static final int RC_PHOTO_PICKER = 1;
    private static final int RESULT_OK = -1;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dbreference;

    private  Uri photouri;

    private RelativeLayout lotrelay;
    private LottieAnimationView lottieAnimationView;

    private  int choose;

    private String fireDb,fireStr;

    public static final int DEFAULT_MEASSAGE_LIMIT_HEAD = 1000;

    public static final int DEFAULT_MEASSAGE_LIMIT = 1000;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private EditText headeredit,descriptionedit,links;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);


        SharedPreferences preferences = getSharedPreferences("chooser",MODE_PRIVATE);
         choose = preferences.getInt("choose",0);
        initialise();
        initialiseclick();
        firbaseinitialise();
    }

    private void firbaseinitialise() {

    if(choose == 0) {
         fireDb = corrector("Al-Ansar-Home");
         fireStr = corrector("Al-Ansar-homeStorage");
    }
    else {
        fireDb = corrector("Up-Comingevents");
        fireStr = corrector("Up-Comingevents-Storage");
    }
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference().child(fireDb);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child(fireStr);
    }
    private void initialiseclick() {

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "documents"), RC_PHOTO_PICKER);

            }
        });
    }

    private void initialise() {

        img = (ImageButton) findViewById(R.id.add);
        headeredit = (EditText) findViewById(R.id.headingChoose);
        descriptionedit = (EditText) findViewById(R.id.descriptionChoose);
        links = (EditText) findViewById(R.id.links);

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
                getMedia();
                 getLotte();
                TastyToast.makeText(getApplicationContext(),"pushing",Toast.LENGTH_SHORT,TastyToast.INFO).show();
                AndroidSystempusher();

                break;
        }
        return true;
    }


    private void AndroidSystempusher() {


      try {
          final String datei = getSystemDate();
          final String headertext = " " + headeredit.getText();
          final String descriptiontext = " " + descriptionedit.getText();
          final String link = String.valueOf(links.getText());

//          Toast.makeText(getApplicationContext(),""+link,Toast.LENGTH_LONG);

          if ((photouri != null) && (headertext.length() > 5) && (descriptiontext.length() > 5)) {
              StorageReference mref = storageReference.child(photouri.getLastPathSegment());


              if (headertext.length() <= 70) {

                  mref.putFile(photouri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                          Uri downloadUrl = taskSnapshot.getDownloadUrl();
                          HomeInitialiser homeInitialiser = new HomeInitialiser(downloadUrl.toString(), "" + headertext.trim().replaceAll("\\s+", " "), " " + descriptiontext.trim().replaceAll("\\s+", " "), datei,link);
                          dbreference.push().setValue(homeInitialiser);

                          TastyToast.makeText(getApplicationContext(), "pushed", Toast.LENGTH_LONG, TastyToast.SUCCESS).show();
                          cancelAnim();
                          finish();
                      }
                  });
                  mref.putFile(photouri).addOnFailureListener(this, new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          TastyToast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG, TastyToast.ERROR).show();
                          cancelAnim();
                      }
                  });

              } else {
                  cancelAnim();
                  TastyToast.makeText(getApplicationContext(), "Heading is too lengthy", Toast.LENGTH_SHORT, TastyToast.ERROR).show();
              }
          } else {
              cancelAnim();

              if (photouri == null) {
                  TastyToast.makeText(getApplicationContext(), "choose photo file", Toast.LENGTH_SHORT, TastyToast.CONFUSING).show();
              } else {
                  TastyToast.makeText(getApplicationContext(), "give heading and description", Toast.LENGTH_SHORT, TastyToast.CONFUSING).show();
              }
          }

      }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
                TastyToast.makeText(getApplicationContext(), "pick an image", Toast.LENGTH_SHORT, TastyToast.INFO).show();

                photouri = data.getData();

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photouri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                img.setImageBitmap(bitmap);
            }
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


    public String getSystemDate() {

        Calendar calendar =Calendar.getInstance();

        return ""+calendar.getTime();
    }
    public void getMedia() {
        final MediaPlayer mp = MediaPlayer.create(getApplicationContext(),R.raw.tweet);
        mp.start();
    }

    public void getLotte() {

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view);
        lottieAnimationView.setAnimation("preloader.json");
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);
        lotrelay = (RelativeLayout) findViewById(R.id.lotteRel);
        lotrelay.setVisibility(View.VISIBLE);
    }
    private void cancelAnim() {

        lottieAnimationView.setVisibility(View.GONE);
        lottieAnimationView.cancelAnimation();
        lotrelay.setVisibility(View.GONE);
    }


}
