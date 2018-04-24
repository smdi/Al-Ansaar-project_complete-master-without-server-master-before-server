package al_muntaqimcrescent2018.com.al_ansar;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdsmdg.tastytoast.TastyToast;
import com.tomer.fadingtextview.FadingTextView;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String fullName = "";
    private String email =  "";
    private FadingTextView longText;
    private CircleImageView navImage;
    private TextView emailTv;
    private static final int RC_File_CHOOSER = 2;
    private WebView webView;
    public static Context getActivity;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActivity = getApplicationContext();
        setVersionNotif();
        initialse();
        setEventLink();
//        getContext();
    }

    public static Context getContext()
    {
        return getActivity;
    }

    private void setEventLink() {
         preferences = getApplicationContext().getSharedPreferences("EventAppLink",MODE_PRIVATE);
         String Event = preferences.getString("AppLink","NODATA");
         if(Event.toString().contains("Events"))
         {
             displaySelectedItem(R.id.comingEvents);
             setEventNull();
         }
         else {

             displaySelectedItem(R.id.hb);
         }
    }

    private void setEventNull() {
        SharedPreferences preferences = getSharedPreferences("EventAppLink",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("AppLink","NODATA");
        editor.commit();
    }

    private void setVersionNotif() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            @SuppressLint("ServiceCast")
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            NotificationChannel notificationChannel = new NotificationChannel(constants.CHANNEL_ID, constants.CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(constants.DESCRIPTION);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[] {100,200,300,400});

            notificationManager.createNotificationChannel(notificationChannel);
        }

    }

    private void initialse() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String emailOfUser = user.getEmail();

        email = ""+emailOfUser;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        webView = (WebView) findViewById(R.id.watsapp);
        navImage =(CircleImageView) headerView.findViewById(R.id.CircularImageOntop);
        longText = (FadingTextView) headerView.findViewById(R.id.onback1);
        longText.setTimeout(FadingTextView.SECONDS ,2);
        emailTv = (TextView) headerView.findViewById(R.id.textView);

        setClicks();
        char chari  =    getTheChar(email);
        String fadad[] = {""+chari,""+chari};
        longText.setTexts(fadad);
        emailTv.setText(email);

    }

    private void setClicks() {


        longText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewAnim(longText,R.id.onback1);
            }
        });

        emailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewAnim(emailTv,R.id.textView);
            }
        });

        navImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewAnim(navImage,R.id.CircularImageOntop);
            }
        });
    }

    private void setAnimation(int id) {
        YoYo.with(Techniques.Shake)
                .duration(700)
                .repeat(1)
                .playOn(findViewById(id));
    }

    public void getViewAnim(View view , final int id) {

        getMedia();
        ViewAnimator
                .animate( view)
                .thenAnimate(view)
                .scale(.2f, 1f, 1f)
                .accelerate()
                .duration(1000)
                .start().onStop(new AnimationListener.Stop() {
            @Override
            public void onStop() {

                setAnimation(id);
            }
        });
    }

    public char  getTheChar(String fullName) {


        String []spliti = fullName.split("\\s");
        String Name;
        int getl = spliti.length;

        if( getl > 0) {


            Name = spliti[spliti.length - 1];
            System.out.println("" + Name.charAt(0));

            return Name.charAt(0);
        }
        else
        {
            System.out.println("" +fullName.charAt(0));

            return  fullName.charAt(0);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.signout) {


            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Do you want to sign out ?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            getMedia();
                            TastyToast.makeText(getApplicationContext() ,"see you soon" ,Toast.LENGTH_SHORT,TastyToast.SUCCESS).show();
                            SharedPreferences.Editor editor = getSharedPreferences("Sign",MODE_PRIVATE).edit();
                            editor.putInt("signIn",0);
                            editor.commit();
                            Intent intent = new Intent(getApplicationContext(),Sign_Up.class);
                            startActivity(intent);
                            FirebaseAuth.getInstance().signOut();
                            finish();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();

                        }
                    });
            android.support.v7.app.AlertDialog alert = builder.create();
            alert.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedItem(int id)
    {
        Fragment fragment = null;

        SharedPreferences preferences = getSharedPreferences("downbutton",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        SharedPreferences preferencesi = getSharedPreferences("EventHome",MODE_PRIVATE);
        SharedPreferences.Editor editori = preferencesi.edit();


        boolean internet = checkConnection();
           getMedia();
           switch (id) {

               case R.id.hb:
                   fragment = new Home();
                   editori.putInt("event", 0);
                   editori.commit();
                   break;

               case R.id.monthly_bayans:

                   if(internet) {
                       fragment = new Monthly_Video_Downloads();
                       editor.putInt("down", 0);
                       editor.commit();

                   }
                   else {

                       setVibrator();
                       getMedia();
                       setConec();
                   }
                   break;
               case R.id.downloads:

                   if(internet) {
                       fragment = new Video_Downloads();
                       editor.putInt("down", 1);
                       editor.commit();

                   }
                   else {

                       setVibrator();
                       getMedia();
                       setConec();
                   }
                   break;
               case R.id.comingEvents:
                   if(internet){
                   fragment = new UpComingEvents();
                   editori.putInt("event", 1);
                   editori.commit();

                       }
                       else {
                       setVibrator();
                       getMedia();
                       setConec();
                   }
                   break;

               case R.id.livebayan:


                   FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                   String emailUser = user.getEmail();

                   if(constants.EMAIL.equals(emailUser)) {


                       if (internet) {

//                          Intent live = new Intent(MainActivity.this,SplashscreenActivity.class);
//                          startActivity(live);

                       } else {
                           setVibrator();
                           getMedia();
                           setConec();
                       }
                   }
                   else{
                       TastyToast.makeText(getApplicationContext(), "User does not have permission", Toast.LENGTH_SHORT, TastyToast.DEFAULT).show();
                   }
                   break;

               case R.id.joingroup:

                   if(internet) {
                       TastyToast.makeText(getApplicationContext(), "processing request", Toast.LENGTH_SHORT, TastyToast.DEFAULT).show();

                       webView.loadUrl("https://chat.whatsapp.com/IFYGuOQouNUF1pdD3g1uXR");
                   }
                   else {
                       setVibrator();
                       getMedia();
                       setConec();
                   }
                   break;

               case R.id.nav_share:

                   if(internet) {
                       Intent i = new Intent(Intent.ACTION_SEND);
                       i.setType("text/plain");
                       i.putExtra(Intent.EXTRA_SUBJECT, "Al Ansaar");
                       String sAux = "\nAl Ansaar App link\n (Spreading peace in world)\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=al_muntaqimcrescent2018.com.al_ansar \n\n";
                       i.putExtra(Intent.EXTRA_TEXT, sAux);
                       startActivity(Intent.createChooser(i, "choose one"));
                   }
                   else {
                       setVibrator();
                       getMedia();
                       setConec();
                   }
                   break;
               case R.id.contact_us:

                   if(internet) {
                       fragment = new Contact();
                   }
                   else {

                       setVibrator();
                       getMedia();
                       setConec();
                   }
                   break;

           }
        if(fragment != null)
        {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment).addToBackStack("tag");
            ft.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    private void setConec() {
        TastyToast.makeText(getApplicationContext(),"Check Internet Connection",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedItem(id);

        return true;
    }
    private boolean checkConnection() {


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            return true;

        } else {
            return false;
        }


    }
    public void getMedia() {
        final MediaPlayer mp = MediaPlayer.create(getApplicationContext() ,R.raw.tweet);
        mp.start();
    }
    private void setVibrator() {
        Vibrator v =(Vibrator)getSystemService(VIBRATOR_SERVICE);
        v.vibrate(1000);
    }
}
