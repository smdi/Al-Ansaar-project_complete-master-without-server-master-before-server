package al_muntaqimcrescent2018.com.al_ansar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdsmdg.tastytoast.TastyToast;
import com.tomer.fadingtextview.FadingTextView;

import java.util.ArrayList;

import developer.shivam.library.DiagonalView;

public class Sign_Up extends AppCompatActivity {

    ArrayList<String> emailA ,userNameA;

    ArrayAdapter<String > arrayAdapter;

    private RelativeLayout lotterelay;

    AutoCompleteTextView emailAc,passwordAc;

    boolean send,check;

    private LottieAnimationView lottieAnimationView;

    TextView signin,signup;

    private FadingTextView fadingTextView;

    Button get,login;
    private FirebaseAuth firebaseAuth;

    private boolean signinb = true ,signupb = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//          this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign__up);

        getSupportActionBar().hide();

        SharedPreferences getSign = getSharedPreferences("Sign",MODE_PRIVATE);
        int sign = getSign.getInt("signIn",0);


        SharedPreferences getShare = getSharedPreferences("Email",MODE_PRIVATE);
        String mail = getShare.getString("email","example@gmail.com");




        if(sign == 1) {


                gotoHome(true ,mail,null);
        }

        else {

            initialise();
            getLotte();
            displayDataInfo();

            AutoCompleteInitialise();

            initialiseSignupandInClicks();

            loginGetButtonInitialise();

            firebaseInitialiser();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser auth = firebaseAuth.getCurrentUser();

    }

    private void firebaseInitialiser() {

        firebaseAuth = FirebaseAuth.getInstance();

    }

    private void loginGetButtonInitialise() {



        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getMedia();
                String email = emailAc.getText().toString().trim();
                String password = passwordAc.getText().toString().trim();

                boolean checklen = getCheck(email,password);
                if(signinb)

                {
                    SharedPreferences.Editor editor = getSharedPreferences("Email", MODE_PRIVATE).edit();
                    editor.putString("email", email);
                    editor.apply();
                    if(checklen) {
                        checkDetails(email, password);
                    }
                    else{
                        setVibrator();
                        getValid("Enter valid details");
                    }
                }
                if(signupb)
                {
                    if(checklen) {
                        createDetails(email, password);
                    }
                    else {
                        setVibrator();
                                getValid("Enter valid details");
                    }
                }

            }
        });


    }

    private void getValid(String s) {

        TastyToast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT,TastyToast.ERROR).show();
    }

    private boolean getCheck(String email, String password) {

        if(email.contains("@gmail.com")&&password.length()>1) {

            return true;
        }
        else {

            return false;
        }
    }

    private void gotoHome(boolean dets ,String email ,String username) {


        if(dets) {

            SharedPreferences.Editor editor = getSharedPreferences("Sign",MODE_PRIVATE).edit();
            editor.putInt("signIn",1);
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            try {

                setAnimVis();
            }
            catch (Exception e){e.printStackTrace();}
            finish();
        }
        else {

            getValid("Enter valid details");
        }


    }

    private void initialiseSignupandInClicks() {

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getMedia();
                TastyToast.makeText(getApplicationContext() ,"sign In",TastyToast.LENGTH_SHORT,TastyToast.INFO).show();


                login.setText("Sign In");

                signinb = true;
                signupb = false;

                signup.setVisibility(View.VISIBLE);
                signin.setVisibility(View.GONE);

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMedia();

                TastyToast.makeText(getApplicationContext() ,"sign Up",TastyToast.LENGTH_SHORT,TastyToast.INFO).show();
                login.setText("Sign Up");

                signinb = false;
                signupb = true;

                signup.setVisibility(View.GONE);
                signin.setVisibility(View.VISIBLE);

            }
        });

    }

    private void AutoCompleteInitialise() {

        int i =1 ;

        if(i==1)
        {
            i=2;
            arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,emailA);
            final AutoCompleteTextView actvGetEmail= (AutoCompleteTextView)findViewById(R.id.email);
            actvGetEmail.setThreshold(1);
            actvGetEmail.setAdapter(arrayAdapter);
            actvGetEmail.setTextColor(Color.BLACK);
            emailAc = actvGetEmail;
        }
        if(i==2)
        {
            i=1;
        }

    }

    private String getUser()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        TastyToast.makeText(getApplicationContext()," Welcome to Al Ansaar ",Toast.LENGTH_LONG,TastyToast.SUCCESS).show();

      return   " "+user.getDisplayName();
    }



    private void initialise() {

        fadingTextView = (FadingTextView) findViewById(R.id.Qtext);
        fadingTextView.setTimeout(FadingTextView.SECONDS ,2);

        signin = (TextView) findViewById(R.id.sign_in);
        signup = (TextView) findViewById(R.id.Sign_up);
        login = (Button) findViewById(R.id.login);

        signup.setTextColor(Color.WHITE);
        signin.setTextColor(Color.WHITE);
        signup.setVisibility(View.VISIBLE);
        signin.setVisibility(View.GONE);

        passwordAc= (AutoCompleteTextView)findViewById(R.id.password);

        emailA = new ArrayList<String >();
        userNameA = new ArrayList<String>();
        get = (Button) findViewById(R.id.login);

    }

    private void createDetails(final String email, String password) {

            lottieAnimationView.setVisibility(View.VISIBLE);
            lotterelay.setVisibility(View.VISIBLE);
            lottieAnimationView.playAnimation();
            login.setEnabled(false);

        firebaseAuth.createUserWithEmailAndPassword(email ,password ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful())
                {

                    gotoHome(true,email,getUser());
                    setAnimVis();

                }
                else {

                    TastyToast.makeText(getApplicationContext(), "Registered User", Toast.LENGTH_SHORT,TastyToast.INFO).show();
                    login.setEnabled(true);
                    setAnimVis();
                }


            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                TastyToast.makeText(getApplicationContext(), " Enter Valid Details ", TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                login.setEnabled(true);
                setAnimVis();
            }
        });

    }

    private void setAnimVis() {

        lottieAnimationView.setVisibility(View.GONE);
        lotterelay.setVisibility(View.GONE);
    }

    private void checkDetails(final String email, String password) {

        lottieAnimationView.setVisibility(View.VISIBLE);
        lotterelay.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
        login.setEnabled(false);

    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {




            if(task.isSuccessful()){

                gotoHome( true,email,getUser());
                setAnimVis();

            }
            else {

                TastyToast.makeText(getApplicationContext(), " Enter Valid Details ", TastyToast.LENGTH_SHORT,TastyToast.CONFUSING).show();
                setAnimVis();
                login.setEnabled(true);
            }


        }
    }).addOnFailureListener(this, new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {

            TastyToast.makeText(getApplicationContext(), " Enter Valid Details ", TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
            setAnimVis();
            login.setEnabled(true);
        }
    });



    }

    private void displayDataInfo() {

        SharedPreferences getShare = getSharedPreferences("Email",MODE_PRIVATE);
        String mail = getShare.getString("email","example@gmail.com");
        emailA.add(""+mail+"");

    }
    public void getMedia() {
        final MediaPlayer mp = MediaPlayer.create(getApplicationContext() ,R.raw.tweet);
        mp.start();
    }
    private void setVibrator() {
        Vibrator v =(Vibrator)getSystemService(VIBRATOR_SERVICE);
        v.vibrate(1000);
    }

    public void getLotte() {

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view);
        lottieAnimationView.setAnimation("preloader.json");
        lottieAnimationView.loop(true);
        lotterelay = (RelativeLayout) findViewById(R.id.lotteRel);
        lotterelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TastyToast.makeText(getApplicationContext(),"please wait",TastyToast.LENGTH_SHORT,TastyToast.DEFAULT);
            }
        });

    }
}
