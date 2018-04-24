package al_muntaqimcrescent2018.com.al_ansar;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Imran on 08-02-2018.
 */

public class Video_Downloads extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FloatingActionButton fab;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dbreference;
    private  ChildEventListener childEventListener;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private LottieAnimationView lottieAnimationView;

    View viewfor;

    ArrayList<Video_Audio_Initialiser> listViewH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video_downloads,container ,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialise(view);
        getLotte();
        getActivity().setTitle("Downloads");
        GetVideos getVideos = new GetVideos();
        getVideos.execute();

    }
    private void initialise(View view) {

        recyclerView = (RecyclerView)  view.findViewById(R.id.text_contact);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        listViewH = new ArrayList();



        fab = (FloatingActionButton) view.findViewById(R.id.fab_montly_downloads);

        fab.setImageResource(R.drawable.videocamera);

        getFab("video",view);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(constants.EMAIL.equals(user.getEmail())) {

            fab.setVisibility(View.VISIBLE);
        }

    }



    private class GetVideos extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {

            String fireDb = corrector( "video-downloads") ;
            String fireStr =  corrector("video-downloads-storage");

            firebaseDatabase = FirebaseDatabase.getInstance();
            dbreference = firebaseDatabase.getReference().child(fireDb);
            firebaseStorage = FirebaseStorage.getInstance();
            storageReference = firebaseStorage.getReference().child(fireStr);



             childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Video_Audio_Initialiser video_audio_initialiser = dataSnapshot.getValue(Video_Audio_Initialiser.class);

                    listViewH.add(video_audio_initialiser);

                    adapter = new Video_Audio_Adapter(getActivity(),listViewH);

                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            dbreference.addChildEventListener(childEventListener);

            return null;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getLotte() {

        lottieAnimationView = (LottieAnimationView) getActivity().findViewById(R.id.animation_view);
        lottieAnimationView.setAnimation("preloader.json");
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy > 10) {
                    lottieAnimationView.cancelAnimation();
                    lottieAnimationView.setVisibility(View.GONE);
                }
                if (dy > 0 || dy < 0 && fab.isShown()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//                    ((MainActivity) getActivity()).getSupportActionBar().hide();
                    if (constants.EMAIL.equals(user.getEmail())) {

                        fab.hide();

                    }

                }
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//                    ((MainActivity) getActivity()).getSupportActionBar().show();
                    if(constants.EMAIL.equals(user.getEmail())) {

                        fab.show();

                    }
                }
            }
        });

    }



    private void getFab(String s,View view) {

        SharedPreferences preferences = this.getActivity().getSharedPreferences("chooser", Context.MODE_PRIVATE);

        SharedPreferences preference = this.getActivity().getSharedPreferences("use", Context.MODE_PRIVATE);


        final SharedPreferences.Editor editor = preferences.edit();

        final SharedPreferences.Editor edito = preference.edit();
        if(s.equals("video")) {

            fab.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {

                    getMedia();
                    TastyToast.makeText(getActivity(),"",TastyToast.LENGTH_SHORT,TastyToast.INFO).show();
                    Intent intent = new Intent(getActivity(),VideoCreator.class);
                    startActivity(intent);

//                    getViewAnim(fab, view);
                    editor.putInt("media", 0);
                    editor.commit();

                    edito.putInt("use", 1);
                    edito.commit();
                }
            });
        }
    }

    private void getViewAnim(FloatingActionButton button, final View view) {

        ViewAnimator
                .animate(button)
                .thenAnimate(button)
                .scale(.1f,
                        1f, 1f)
                .accelerate()
                .duration(2000)
                .start().onStop(new AnimationListener.Stop() {
            @Override
            public void onStop() {

                Intent intent = new Intent(getActivity(),VideoCreator.class);
                startActivity(intent);
            }
        });

    }

    public void getMedia() {
        final MediaPlayer mp = MediaPlayer.create(getActivity(),R.raw.tweet);
        mp.start();
    }
}
