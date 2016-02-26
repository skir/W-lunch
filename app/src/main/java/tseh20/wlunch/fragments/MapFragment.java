package tseh20.wlunch.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.ButterKnife;
import tseh20.wlunch.R;

/**
 * Created by ilia on 22.02.16.
 */
public class MapFragment extends Fragment {

    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, rootView);

        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync((GoogleMap googleMap) -> {
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(50.456150, 30.496640))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_coffee));
            googleMap.addMarker(markerOptions);

            googleMap.setOnMarkerClickListener((Marker marker) -> {
                CustomDialogFragment dialog = new CustomDialogFragment();
                dialog.setId(1);
                dialog.show(getChildFragmentManager(), "tag");
                return false;
            });
        });
        return rootView;
    }
}
