package al_muntaqimcrescent2018.com.al_ansar;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sdsmdg.tastytoast.TastyToast;
import com.tomer.fadingtextview.FadingTextView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Imran on 31-01-2018.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<HomeInitialiser> listitem;
    private Context context;
    private View myview;
    private  boolean change = true,liked = false;
    private int position,pos;

    public EventAdapter(Context context, List<HomeInitialiser> listitem) {
        this.listitem = listitem;
        this.context = context;
    }

       public String getCapsHead(String heading) {


        StringBuffer  str = new StringBuffer();

        String build = new String();
        String fullName = ""+heading;

        String []bank = fullName.split("\\s");

        String token ,remain;


        if(true) {

            try {
                for(int i = 0 ;i<bank.length ;i++){

                    token = bank[i].substring(0,1);

                    remain = bank[i].substring(1,bank[i].length());

                    str.append(" "+build.concat(token.toUpperCase()+remain));

                    System.out.print(" "+build.concat(token.toUpperCase()+remain));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return " "+str;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView  headings  ,more;
        public FadingTextView dates;
        public TextView  descriptions;
        public ImageView cimages,download,links;
        public RelativeLayout relay;
        public ImageView share;
        public WebView webView;
        public  int event;


        public ViewHolder(View itemView) {
            super(itemView);

            myview = itemView;
            download = (ImageView) itemView.findViewById(R.id.downloadbut);
            dates = (FadingTextView) itemView.findViewById(R.id.date);
            more = (TextView) itemView.findViewById(R.id.more);
            headings = (TextView) itemView.findViewById(R.id.main_head);
            descriptions = (TextView) itemView.findViewById(R.id.description);
            cimages = (ImageView) itemView.findViewById(R.id.CircularImageOntop);
            relay = (RelativeLayout) itemView.findViewById(R.id.Layout_inCard);
            share = (ImageView) itemView.findViewById(R.id.share);
            webView = (WebView) itemView.findViewById(R.id.watsapp);
            links = (ImageView) itemView.findViewById(R.id.linkImage);
            SharedPreferences sharedPreferences= context.getSharedPreferences("EventHome",Context.MODE_PRIVATE);
            event = sharedPreferences.getInt("event",0);
        }
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item,parent,false);

        return new ViewHolder(v);
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
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final HomeInitialiser homeInitialiser = listitem.get(position);

        pos = position+1;

        String headerGet = getCapsHead(homeInitialiser.getHeading());

        holder.headings.setText(""+headerGet.replaceAll("\\s+"," "));


        holder.headings.setTypeface(Typeface.MONOSPACE);



        if(homeInitialiser.getLink().equals(""))
        {
                holder.links.setVisibility(View.GONE);
        }
        else {
            holder.links.setVisibility(View.VISIBLE);
        }


        holder.links.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getMedia();

                Intent intent = new Intent(context,Fundamentals.class);
                intent.putExtra("link",""+homeInitialiser.getLink());
                context.startActivity(intent);
            }
        });

        Toast.makeText(context,""+homeInitialiser.getLink(),Toast.LENGTH_LONG);

        holder.descriptions.setText(""+homeInitialiser.getDescription().replaceAll("\\s+"," "));

        holder.descriptions.setTypeface(Typeface.MONOSPACE);

        if(homeInitialiser.getDescription().length()>100)
        {
            holder.more.setVisibility(View.VISIBLE);
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getMedia();
                    getTextChange(holder);
                }
            });

        }
        else {

            holder.more.setVisibility(View.GONE);
        }
        holder.cimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMedia();
                Intent intt = new Intent(context,Fundamentals.class);
                intt.putExtra("link",""+homeInitialiser.getUri());
                context.startActivity(intt);

            }
        });

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getMedia();
                setURl(homeInitialiser.getUri(),homeInitialiser);
            }
        });

        boolean isphoto = homeInitialiser.getUri() != null;


        if(isphoto)
        {
            Glide.with(context).load(homeInitialiser.getUri())
                    .into(holder.cimages);

        }
        else {

            holder.cimages.setVisibility(View.GONE);

        }

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getMedia();

               try {

                   Intent share =    shareImageData(context ,""+homeInitialiser.getHeading(), ""+homeInitialiser.getUri() ,""+homeInitialiser.getDescription(),homeInitialiser.getLink());

                   context.startActivity(Intent.createChooser(share, "choose one"));

               }
                catch (Exception e){e.printStackTrace();}

            }
        });


        holder.dates.setText(""+getTheTime(homeInitialiser.getDate()));

        StringBuilder check = getTheTime(""+getSystemDate());

        StringBuilder chari = getTheTime(""+homeInitialiser.getDate());

        String fadad[] = {""+chari,""+chari};
        if(check.toString().equals(chari.toString())) {
            holder.dates.setTimeout(FadingTextView.SECONDS ,2);
            holder.dates.setTexts(fadad);
            holder.dates.setTextColor(Color.BLACK);
        }
        else {
           holder.dates.setTimeout(FadingTextView.SECONDS ,2);
            holder.dates.setTexts(fadad);
            holder.dates.setTextColor(Color.GRAY);
        }



    }


    private void setURl(String downloadUrl, HomeInitialiser homeInitialiser) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));

        request.allowScanningByMediaScanner();

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+homeInitialiser.getHeading().trim()+".png");

        DownloadManager dm = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);

        dm.enqueue(request);

        TastyToast.makeText(context,"downloading file",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS).show();
    }
    public static Intent shareImageData(Context context, String header, String link, String description,String linkOpen) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        if (Build.VERSION.SDK_INT  < Build.VERSION_CODES.LOLLIPOP) {

              shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }
        else {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }


        String textins = "";
        SharedPreferences sharedPreferences= context.getSharedPreferences("EventHome",Context.MODE_PRIVATE);
        int event = sharedPreferences.getInt("event",0);
        if(event == 0)
        {
            textins = "Al Ansaar Home Recommendations "+"\n\nAl Ansaar has New Feed in Home";
        }
        else {
            textins = "Al Ansaar Event Recommendations "+"\n\nAl Ansaar has New Feed in Events";
        }

        String applink = "https://play.google.com/store/apps/details?id=al_muntaqimcrescent2018.com.al_ansar";

        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, header);
        String sAux ="\n"+" بسم الله الرحمن الرحيم " + "\n\n" + textins+"\n\n" +header+"\n";
        sAux = sAux+"\n"+description+"\n";
        if(link.equals(""))
        {
        }
        else {
            sAux = sAux +"\n follow link for open image \n"+ link +"\n";
        }
        if(linkOpen.equals(""))
        {
        }
        else {
               sAux = sAux +"\n follow link for more information \n"+ linkOpen +"\n";
        }
        if(event == 0)
        {
            sAux = sAux +"\nFollow link to open Al Ansaar\n"+ "http://alansaar.onuniverse.com/youtu.be/Main\n";
        }
        else {
            sAux = sAux +"\nFollow link to open Al Ansaar\n"+ "http://alansaar.onuniverse.com/youtu.be/Events\n";
        }

        sAux = sAux +"\nFollow link to download Al Ansaar (Spreading peace in the world)\n"+applink+"\n\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, sAux);

        return shareIntent;
    }



    private void getTextChange(ViewHolder holder) {
        if (change) {

            holder.descriptions.setMaxLines(Integer.MAX_VALUE);
            holder.more.setText("show less");
            change = false;
        } else {

            holder.more.setText("show more");
            change = true;
            holder.descriptions.setMaxLines(3);

        }
    }
    public String getSystemDate() {

        Calendar calendar =Calendar.getInstance();

        return ""+calendar.getTime();
    }
    @Override
    public int getItemCount() {
        return listitem.size();
    }
    public void getMedia() {
        final MediaPlayer mp = MediaPlayer.create(context,R.raw.tweet);
        mp.start();
    }
}
