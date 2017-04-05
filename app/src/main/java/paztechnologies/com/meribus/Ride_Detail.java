package paztechnologies.com.meribus;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Admin on 3/24/2017.
 */

public class Ride_Detail extends Fragment implements OnMapReadyCallback, View.OnClickListener, DirectionCallback {
    String Server_Key = "AIzaSyCdUU47miOD97PDox1FwhGr3SHPkbG4A2s";
    private GoogleMap googleMap;
    private MapView mMapView;
    private LatLng camera = new LatLng(28.6158851, 77.0406466);
    private LatLng curr = new LatLng(28.6158851, 77.0406466);
    private LatLng des = new LatLng(28.6168968, 77.0459028);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ride_detail, container, false);
        MapsInitializer.initialize(this.getActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //        if (google == null) {
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            google = SupportMapFragment.newInstance();
//            fragmentTransaction.replace(R.id.map, google).commit();
//        }
        requestDirection();
        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        //  Snackbar.make(getView(), "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "Success with status : " + direction.getStatus(), Toast.LENGTH_SHORT).show();

        if (direction.isOK()) {
            googleMap.addMarker(new MarkerOptions().position(curr));
            googleMap.addMarker(new MarkerOptions().position(des));

            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            googleMap.addPolyline(DirectionConverter.createPolyline(getActivity(), directionPositionList, 5, Color.RED));

        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        //   Snackbar.make(getView(), t.getMessage(), Snackbar.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(camera, 15));

//        LatLng latlong = new LatLng(28.6158851,77.0406466);
//        googleMap.addMarker(new MarkerOptions().position(latlong)
//                .title("india"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong,10));
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
    }

    public void requestDirection() {
        // Snackbar.make(getView(), "Direction Requesting...", Snackbar.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "Direction Requesting...", Toast.LENGTH_SHORT).show();
        GoogleDirection.withServerKey(Server_Key)
                .from(curr)
                .to(des)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }

}
