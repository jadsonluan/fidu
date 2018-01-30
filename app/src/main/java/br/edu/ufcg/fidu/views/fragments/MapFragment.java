package br.edu.ufcg.fidu.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.views.activities.SearchDoneeActivity;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private SupportMapFragment mSupportMapFragment;
    private View mRootView;
    private static double mLat, mLng;

    public static MapFragment newInstance(double lat, double lng) {
        MapFragment.mLat = lat;
        MapFragment.mLng = lng;
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSupportMapFragment = SupportMapFragment.newInstance();
        mRootView = inflater.inflate(R.layout.fragment_map, null);
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.beginTransaction().replace(R.id.frameLayout_map, mSupportMapFragment).commitAllowingStateLoss();
        }
        mSupportMapFragment.getMapAsync(this);

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDonees();
            }
        });
    }

    private void searchDonees() {
        startActivity(new Intent(getActivity(), SearchDoneeActivity.class));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(MapFragment.mLat, MapFragment.mLng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));
    }
}
