package al_muntaqimcrescent2018.com.al_ansar;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

public class livebayan extends AppCompatActivity {


   private int del;
    private boolean web = true , text = true;
   private String url;
   private LottieAnimationView lottieAnimationView;
   private FirebaseDatabase firebaseDatabase;
   private DatabaseReference dbreference;
   private StorageReference storageReference;
   private FirebaseStorage firebaseStorage;
   private  WebSettings webSettings;
   private WebView webView ;
    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED ,WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.live);

        getLotte();
        getSupportActionBar().hide();

        url = getIntent().getExtras().getString("link");
        webView = (WebView) findViewById(R.id.my_web_view);
        webSettings = webView.getSettings();
        webView.setWebViewClient(new MyBrowser(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                lottieAnimationView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
               setCancel();
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
//        webView.getSettings().setJavaScriptEnabled(true);
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


    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }



    private void setCancel() {
        lottieAnimationView.cancelAnimation();
        lottieAnimationView.setVisibility(View.GONE);
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


