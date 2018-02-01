package br.edu.ufcg.fidu.utils;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.models.Donee;
import br.edu.ufcg.fidu.models.User;
import br.edu.ufcg.fidu.views.activities.DoneeProfileActivity;
import br.edu.ufcg.fidu.views.activities.MainActivity;

public class DoneeAdapter extends BaseAdapter {

    private final Activity activity;
    private final ArrayList<Donee> donees;
    private final User user;

    private ImageView photo;

    public DoneeAdapter(ArrayList<Donee> donees, User user, Activity activity) {
        this.donees = donees;
        this.user = user;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return donees.size();
    }

    @Override
    public Object getItem(int i) {
        return donees.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = activity.getLayoutInflater()
                .inflate(R.layout.item_donee, viewGroup, false);

        final Donee donee = donees.get(i);
        photo = view.findViewById(R.id.profilePhoto);
        TextView name = view.findViewById(R.id.tvName);
        TextView address = view.findViewById(R.id.tvAddress);

        loadPhoto(donee.getPhotoUrl(), photo);

        name.setText(donee.getName());
        String unknown = activity.getString(R.string.unknown_information);

        if (donee.getLat() != 0 && donee.getLng() != 0) {
            LatLng doneeLocation = new LatLng(donee.getLat(), donee.getLng());
            LatLng userLocation = new LatLng(user.getLat(), user.getLng());
            double distance = calculateDistance(userLocation, doneeLocation);
            address.setText(String.format("%.2f km", distance));
        } else {
            address.setText("localização não informada");
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DoneeProfileActivity.class);
                intent.putExtra("donee_uid", donee.getUid());
                intent.putExtra("user_uid", user.getUid());
                activity.startActivity(intent);
            }
        });

        return view;
    }

    private double calculateDistance(LatLng start, LatLng end) {
        float[] results = new float[1];
        Location.distanceBetween(start.latitude, start.longitude,
                end.latitude, end.longitude, results);
        return results[0];
    }

    /**
     * Carrega uma imagem em um ImageView especificado
     *
     * @param url URL da imagem a ser carregada
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
