package al_muntaqimcrescent2018.com.al_ansar;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdsmdg.tastytoast.TastyToast;

public class Fundamentals extends AppCompatActivity {


   private int del;
    private boolean web = true , text = true;
   private String url;
   private LottieAnimationView lottieAnimationView;
   private FirebaseDatabase firebaseDatabase;
   private DatabaseReference dbreference;
   private StorageReference storageReference;
   private FirebaseStorage firebaseStorage;
   private FloatingActionButton fab;
   private  WebSettings webSettings;
   private WebView webView ;
    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED ,WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_fundamentals);

        getLotte();
        getFab();
        getSupportActionBar().hide();

        url = getIntent().getExtras().getString("link");

        if(url.contains("source.coolmic.net")){

        }
        else {

        }


        webView = (WebView) findViewById(R.id.my_web_view);
        webSettings = webView.getSettings();

        webView.setWebViewClient(new MyBrowser(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webView.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
               setCancel();

                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByTagName('height')[0].innerHTML=100% ; })()");
            }
        });



        webView.setWebChromeClient(new WebChromeClient(){
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVisibility(View.VISIBLE);
        webView.loadUrl(url);
//        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
       webSettings.getUseWideViewPort();
        webSettings.setLoadWithOverviewMode(true) ;
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.getSaveFormData();
        webSettings.setEnableSmoothTransition(true);
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setBuiltInZoomControls(true);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        }
        else{
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        }

        this.registerForContextMenu(webView);
    }


    public void getFab() {

        fab = (FloatingActionButton) findViewById(R.id.fab_delete);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.getEmail().equals(constants.EMAIL))
        {
            fab.setVisibility(View.VISIBLE);
        }
        else {
            fab.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMedia();setVibrator();
               getLotte();
                String fireDb="" ;
                String fireStr="";
                SharedPreferences preferencesi = getSharedPreferences("EventHome",MODE_PRIVATE);
                del = preferencesi.getInt("event",0);
                if(del == 0)
                {
                    fireDb = "Al-Ansar-Home";
                    fireStr = "Al-Ansar-homeStorage";
                }
                else  if(del == 1)
                {
                     fireDb = "Up-Comingevents" ;
                     fireStr = "Up-Comingevents-Storage";
                }
                if(del == 0)
                {
                    getRemove("Al-Ansar-Home","uri",fireStr);
                }
                else {
                    getRemove("Up-Comingevents","uri",fireStr);
                }
            }
        });
    }
    private void getDeleteStore(String fireStr) {
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child(fireStr);


        StorageReference storageReference =firebaseStorage.getReferenceFromUrl(url);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                TastyToast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT,TastyToast.SUCCESS).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                TastyToast.makeText(getApplicationContext(),"try after some time",Toast.LENGTH_SHORT,TastyToast.ERROR).show();
            }
        });
    }

    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void getRemove(String s, String s1, final String fireStr) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query Uri = reference.child(s).orderByChild(s1).equalTo(url);

        Uri.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot uri : dataSnapshot.getChildren() )
                {

                    uri.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            TastyToast.makeText(getApplicationContext(),"deleted the database",Toast.LENGTH_SHORT,TastyToast.SUCCESS).show();

                            getDeleteStore(fireStr);

                           setCancel();
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                TastyToast.makeText(getApplicationContext(),"May be deleted !",Toast.LENGTH_SHORT,TastyToast.ERROR).show();
                setCancel();
                finish();
            }
        });

    }

    private void setCancel() {
        lottieAnimationView.cancelAnimation();
        lottieAnimationView.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
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

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.loadvideo);
        lottieAnimationView.setAnimation("preloader.json");
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);

    }
}


