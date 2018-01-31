package br.edu.ufcg.fidu.views.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.models.User;
import br.edu.ufcg.fidu.utils.SaveData;
import br.edu.ufcg.fidu.views.activities.SearchDoneeActivity;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private SupportMapFragment mSupportMapFragment;
    private View mRootView;
    private View btnSearch;
    private static double mLat, mLng;
    private Marker myLocation;

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

        myLocation = null;

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout_map, mSupportMapFragment)
                    .commitAllowingStateLoss();
        }
        mSupportMapFragment.getMapAsync(this);

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDonees();
            }
        });

        checkUser();
    }

    private void checkUser() {
        SaveData sv = new SaveData(getActivity());

        if (sv.isLogged()) {
            if (sv.getRole() == SaveData.DONEE) {
                btnSearch.setVisibility(View.GONE);
            }
        }
    }

    private void searchDonees() {
        startActivity(new Intent(getActivity(), SearchDoneeActivity.class));
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLng latLng = new LatLng(MapFragment.mLat, MapFragment.mLng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                selectPosition(latLng, googleMap);
            }
        });
    }

    private void selectPosition(final LatLng point, final GoogleMap map) {
        new AlertDialog.Builder(getActivity())
//                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.dialog_title_update_location))
                .setMessage(getString(R.string.dialog_body_update_location))
                .setNegativeButton(getString(R.string.no), null)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (myLocation != null) myLocation.remove();
                        myLocation = map.addMarker(new MarkerOptions().position(point).title("Minha localização"));
                        updateLocation(point);
                    }
                })
                .show();
    }

    private void updateLocation(LatLng point) {

    }
}
