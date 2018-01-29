package br.edu.ufcg.fidu.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.models.Donee;

/**
 * Created by luan on 28/01/18.
 */

public class DoneeAdapter extends BaseAdapter {

    private final Activity activity;
    private final ArrayList<Donee> donees;

    public DoneeAdapter(ArrayList<Donee> donees, Activity activity) {
        this.donees = donees;
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

        Donee donee = donees.get(i);
        ImageView photo = view.findViewById(R.id.profilePhoto);
        TextView name = view.findViewById(R.id.tvName);
        TextView address = view.findViewById(R.id.tvAddress);

//        Glide
//            .with(photo.getContext())
//            .load()
//            .into(photo);

        name.setText(donee.getName());
        String unknown = activity.getString(R.string.unknown_information);
        address.setText(donee.getAddress() == null ? unknown : donee.getAddress());

        return view;
    }
}
