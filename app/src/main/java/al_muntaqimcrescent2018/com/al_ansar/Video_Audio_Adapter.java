package al_muntaqimcrescent2018.com.al_ansar;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.florent37.viewanimator.ViewAnimator;
import com.sdsmdg.tastytoast.TastyToast;
import com.tomer.fadingtextview.FadingTextView;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;

/**
 * Created by Imran on 08-02-2018.
 */

public class Video_Audio_Adapter extends RecyclerView.Adapter<Video_Audio_Adapter.ViewHolder> {

    private int pos;
    private Context context;
    private List<Video_Audio_Initialiser> listitem;

    public Video_Audio_Adapter(Context context, List<Video_Audio_Initialiser> listitem) {
        this.context = context;
        this.listitem = listitem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list,parent,false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


//        getViewAnim(holder.videoView);
        final Video_Audio_Initialiser video_audio_initialiser = listitem.get(position);

        holder.mediades.setText(""+video_audio_initialiser.getDescription().replaceAll("\\s+","  "));

        StringBuilder date = getTheTime(""+video_audio_initialiser.getDate());

        StringBuilder check = getTheTime(""+getSystemDate());

        String fadad[] = {""+date,""+date};
        if(date.toString().equals(check.toString())) {

            holder.datemedia.setTimeout(FadingTextView.SECONDS ,2);
            holder.datemedia.setTexts(fadad);
            holder.datemedia.setTextColor(Color.BLACK);
        }
        else {
            holder.datemedia.setTimeout(FadingTextView.SECONDS ,2);
            holder.datemedia.setTexts(fadad);
            holder.datemedia.setTextColor(Color.GRAY);
        }
        final String vid = getVid(""+video_audio_initialiser.getUri());


        holder.videoView.setWebViewClient(new MyBrowser(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                getLotte(holder);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
               holder.lottieAnimationView.setVisibility(View.GONE);
            }
        });

        holder.videoView.loadUrl("http://www.youtube.com/embed/"+vid+"?autoplay=1&vq=large"+"&rel=0");


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMedia();
                SharedPreferences preferences =context.getSharedPreferences("NOTIFY",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("notify",1);
                editor.commit();
                Intent intent = new Intent(context,AV_display.class);
                intent.putExtra("link",""+video_audio_initialiser.getUri());
                context.startActivity(intent);
            }
        });

        holder.shareview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getMedia();

                final Video_Audio_Initialiser homeInitialiser = listitem.get(position);
                Intent share =    shareImageData(context ,"", ""+homeInitialiser.getUri() ,""+homeInitialiser.getDescription());

                context.startActivity(Intent.createChooser(share, "choose one"));

            }
        });

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getMedia();

                final Video_Audio_Initialiser homeInitialiser = listitem.get(position);

                try {

                    YouTubeUriExtractor ytEx = new YouTubeUriExtractor(context) {
                        @Override
                        public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {
                            if (ytFiles != null) {
                                int itag = 22;

                                try {
                                    String downloadUrl = ytFiles.get(itag).getUrl();

                                    setURl(downloadUrl,homeInitialiser);
                                }
                                catch (Exception e){e.printStackTrace();}
                            }
                        }
                    };
                    try {
                        ytEx.execute(homeInitialiser.getUri());

                        TastyToast.makeText(context ,"checking possibility", Toast.LENGTH_LONG,TastyToast.DEFAULT).show();
                    }
                    catch (Exception e){e.printStackTrace();}
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        getWebFast(holder);

    }

//    private void getViewAnim(WebView videoView) {
//
//        ViewAnimator
//                .animate( videoView)
//                .thenAnimate(videoView)
//                .scale(.1f,
//                        1f, 1f)
//                .accelerate()
//                .duration(1000)
//                .start();
//
//    }

    public String getSystemDate() {

        Calendar calendar =Calendar.getInstance();

        return ""+calendar.getTime();
    }
    private void setURl(String downloadUrl, Video_Audio_Initialiser homeInitialiser) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));

        request.allowScanningByMediaScanner();

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+homeInitialiser.getDescription().trim()+".mp4");

        DownloadManager dm = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);

        dm.enqueue(request);

        TastyToast.makeText(context ,"downloading file",Toast.LENGTH_SHORT,TastyToast.SUCCESS).show();
    }

    public void getWebFast(ViewHolder holder) {

        holder.videoView.getSettings().setPluginState(WebSettings.PluginState.ON);
        holder.videoView.getSettings().setJavaScriptEnabled(true);
        holder.videoView.setVisibility(View.VISIBLE);
        holder.videoView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        holder.videoView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        holder.videoView.getSettings().setAppCacheEnabled(true);
        holder.videoView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
//        holder.videoView.setScrollbarFadingEnabled(true);
        holder.webSettings.setDomStorageEnabled(true);
        holder.webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        holder.webSettings.getUseWideViewPort();
        holder.webSettings.setLoadWithOverviewMode(true);
        holder.webSettings.setUseWideViewPort(true);
        holder.webSettings.setSupportZoom(true);
        holder.webSettings.getSaveFormData();
        holder.webSettings.setEnableSmoothTransition(true);
//        holder.videoView.getSettings().setJavaScriptEnabled(true);
        holder.videoView.setVisibility(View.VISIBLE);
//        holder.videoView.getSettings().setBuiltInZoomControls(true);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            holder.videoView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        }
        else{
            holder.videoView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        }

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    public StringBuilder getTheTime(String time) {


        StringBuilder stringBuilder = new StringBuilder();
        String split[]  =  time.split("\\s");
        for(int i=0; i<3; i++)
        {

            stringBuilder.append("  "+split[i]);
        }

        stringBuilder.append(" "+split[5]);


        return stringBuilder;
    }
    @Override
    public int getItemCount() {
        return listitem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public WebView videoView;
        public LottieAnimationView lottieAnimationView;
        public RelativeLayout relativeLayout ,mainrelay;
        public TextView mediades;
        public FadingTextView datemedia;
        public ImageButton imageView,shareview,download;
        public WebSettings webSettings;
        public ViewHolder(View itemView) {
            super(itemView);

            download = (ImageButton) itemView.findViewById(R.id.downloadOption);
            shareview = (ImageButton) itemView.findViewById(R.id.share);
            imageView = (ImageButton) itemView.findViewById(R.id.enlarge);
            videoView = (WebView) itemView.findViewById(R.id.CircularImageOntop);
            mediades  = (TextView)  itemView.findViewById(R.id.descriptionmedia);
            datemedia = (FadingTextView)  itemView.findViewById(R.id.datemedia);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.Layout_inCard);
            lottieAnimationView = (LottieAnimationView) itemView.findViewById(R.id.loadvideo);
            webSettings = videoView.getSettings();
            mainrelay = (RelativeLayout) itemView.findViewById(R.id.mainrelay);
            SharedPreferences preferences = context.getSharedPreferences("downbutton",Context.MODE_PRIVATE);
            int hide  = preferences.getInt("down",0);
            if(hide == 1)
            {
                download.setVisibility(View.VISIBLE);
            }
            else {
                download.setVisibility(View.GONE);
            }
        }
    }


    public String getVid(String text) {

        String yt = ""+text;
        final String []ty ;

        String ret = "";
        if(yt.contains("=")) {
            final String[] you = yt.split("=");

            if (you[1].contains("&")) {
                ty = you[1].split("&");


                System.out.println("" + ty[0]);

                ret = ty[0];
            } else {


                System.out.println("" + you[1]);

                ret = you[1];
            }

        return  ret;
        }
        else {

            final String[] you = yt.split("https://youtu.be/");

            if (you[1].contains("&")) {
                ty = you[1].split("&");


                System.out.println("" + ty[0]);

                ret = ty[0];
            } else {

                System.out.println("" + you[1]);

                ret = you[1];
            }

            return  ret;

        }

    }
    public static Intent shareImageData(Context context, String header, String link, String description) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        if (Build.VERSION.SDK_INT  < Build.VERSION_CODES.LOLLIPOP) {

            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }
        else {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }

        String textins = "";
        SharedPreferences preferences = context.getSharedPreferences("downbutton",Context.MODE_PRIVATE);
        int hide  = preferences.getInt("down",0);
        if(hide == 1)
        {
           textins = "Al Ansaar Download Recommendations "+"\n\nMeida is Available for Download";
        }
        else {
            textins = "Al Ansaar Bayan Recommendations ";
        }

           String applink = "https://play.google.com/store/apps/details?id=al_muntaqimcrescent2018.com.al_ansar";

        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, header);
        String sAux ="\n"+ " بسم الله الرحمن الرحيم "+ "\n\n"+ textins +"\n\n";
        sAux = sAux+""+description+"\n\n";
        sAux = sAux + "\nFollow link to view in Al Ansaar \n"+link.replace("https://youtu.be/","http://alansaar.onuniverse.com/youtu.be/")+"\n";
        sAux = sAux +"\nFollow link to view in youtube\n"+link+"\n";
        sAux = sAux +"\nFollow link to download Al Ansaar (Spreading peace in the world)\n"+applink+"\n\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, sAux);

        return shareIntent;
    }

    public void getMedia() {
        final MediaPlayer mp = MediaPlayer.create(context,R.raw.tweet);
        mp.start();
    }
    public void getLotte(ViewHolder holder) {
        holder.lottieAnimationView.setAnimation("preloader.json");
       holder. lottieAnimationView.setVisibility(View.VISIBLE);
        holder.lottieAnimationView.playAnimation();
       holder.lottieAnimationView.loop(true);

    }
}
