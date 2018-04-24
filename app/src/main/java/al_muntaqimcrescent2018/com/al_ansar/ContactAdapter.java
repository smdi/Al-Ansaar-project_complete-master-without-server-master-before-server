package al_muntaqimcrescent2018.com.al_ansar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Imran on 06-02-2018.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {


    private Context context;
    private List<ContactInitialiser> listView;

    public ContactAdapter(Context context ,List<ContactInitialiser> listView) {
        this.context = context;
        this.listView=listView;
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list,parent ,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ContactInitialiser contactInitialiser = listView.get(position);

        String headerGet = getCapsHead(contactInitialiser.getName());
        String proff = getCapsHead(contactInitialiser.getProfession());
        holder.contactName.setText(""+headerGet);
        holder.profession.setText(""+proff);
        holder.mail.setText(""+contactInitialiser.getMail());

        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user.getEmail().equals(constants.EMAIL))
                {

                    goDelete(context ,contactInitialiser.getMail());

                }

            }
        });

        holder.notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user.getEmail().equals(constants.EMAIL)) {

                    SharedPreferences preferences = context.getSharedPreferences("NOTIFY", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("notify", 0);
                    editor.commit();
                    Intent intent = new Intent(context, AV_display.class);
                    intent.putExtra("link", "https://console.firebase.google.com/u/2/project/al-ansaar-f950b/notification/compose");
                    context.startActivity(intent);

                }
            }
        });
    }

    private void goDelete(final Context context, String mail) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query Uri = reference.child("Contact-list").orderByChild("mail").equalTo(mail);

        Uri.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot uri : dataSnapshot.getChildren() )
                {

                    uri.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            TastyToast.makeText(context,"deleted the database",Toast.LENGTH_SHORT,TastyToast.SUCCESS).show();


                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return listView.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView contactName;
        public TextView  profession;
        public TextView  mail;
        public ImageView mailIcon,notify;

        public ViewHolder(View itemView) {
            super(itemView);

            notify = (ImageView) itemView.findViewById(R.id.alansarcontacts);
            mailIcon = (ImageView) itemView.findViewById(R.id.mailIcon);
            contactName = (TextView) itemView.findViewById(R.id.contactName);
            profession  = (TextView) itemView.findViewById(R.id.profession);
            mail        = (TextView) itemView.findViewById(R.id.mail);


        }
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

}
