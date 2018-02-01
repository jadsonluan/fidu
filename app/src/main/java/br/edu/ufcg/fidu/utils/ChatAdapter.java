package br.edu.ufcg.fidu.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.models.User;
import br.edu.ufcg.fidu.views.activities.ChatActivity;

public class ChatAdapter extends BaseAdapter {

    private final String uid;
    private final Activity activity;
    private final boolean isDonee;
    private ArrayList<String> otherUIDs;

    public ChatAdapter(String uid, boolean isDonee, ArrayList<String> otherUIDs, Activity activity) {
        this.uid = uid;
        this.isDonee = isDonee;
        this.otherUIDs = otherUIDs;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return otherUIDs.size();
    }

    @Override
    public Object getItem(int i) {
        return otherUIDs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final View view = activity.getLayoutInflater()
                .inflate(R.layout.item_message, viewGroup, false);

        final ImageView photo = view.findViewById(R.id.profilePhoto);
        final TextView name = view.findViewById(R.id.tvName);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String role = isDonee ? "donors" : "donees";

        Query query = mDatabase.child("users").child(role).child(otherUIDs.get(i));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User other = dataSnapshot.getValue(User.class);

                if (other != null) {
                    name.setText(other.getName());

                    loadPhoto(other.getPhotoUrl(), photo);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openChat(other.getUid(), other.getName());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return view;
    }

    private void openChat(String otherId, String othername) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra(ChatActivity.USER_UID, uid);
        intent.putExtra(ChatActivity.OTHER_UID, otherId);
        intent.putExtra(ChatActivity.OTHERNAME, othername);
        activity.startActivity(intent);
    }

    /**
     * Carrega uma imagem em um ImageView especificado
     *
     * @param url UID do usuário que terá sua foto carregada
     * @param image componente onde a imagem será carregada
     */
    private void loadPhoto(String url, ImageView image) {
        if (!url.equals("")) {
            Glide.with(image.getContext())
                    .load(url)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            e.printStackTrace();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(image);
        }
    }
}
