package john.aboutstate.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import john.aboutstate.R;

/**
 * Created by john on 28.11.14.
 */
public class MapStateFragment extends Fragment {
    private static View rootView;
    private static GoogleMap map;
    double latitude_state,longitude_state,area_state;
    ImageView saveButton,slideButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        saveButton  = (ImageView) getActivity().findViewById(R.id.save_state_imgv);
        slideButton  = (ImageView) getActivity().findViewById(R.id.slide_menu);
        saveButton.setVisibility(View.INVISIBLE);
        slideButton.setVisibility(View.INVISIBLE);

        Bundle bundle = getArguments();
        latitude_state = Double.parseDouble(bundle.getString("lat"));
        longitude_state = Double.parseDouble(bundle.getString("lng"));
        area_state = Double.parseDouble(bundle.getString("area"));

        setRetainInstance(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(container==null){
            return null;
        }

        rootView = inflater.inflate(R.layout.state_on_map,container,false);
        setUpMapIfNeeded();
        return rootView;
    }

    public  void setUpMapIfNeeded() {
        if (map==null){
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.google_map)).getMap();
        if (map !=null ){
            setUpMap();
        }
        }
    }

    public void setUpMap(){
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        map.addCircle(new CircleOptions()
                .center(new LatLng(latitude_state, longitude_state))
                .radius(Math.sqrt(area_state/Math.PI) * 1000)
                .strokeWidth(0f)
                .fillColor(0x550000FF));

        LocationManager locationManager = (LocationManager)getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.RED)
                .width(3)
                .add(new LatLng(latitude_state, longitude_state))
                .add(new LatLng( myLocation.getLatitude(),myLocation.getLongitude()));
        map.addPolyline(polylineOptions);

        Location locationA = new Location("");
        locationA.setLatitude(latitude_state);
        locationA.setLongitude(longitude_state);

        Location locationB = new Location("");
        locationB.setLatitude(myLocation.getLatitude());
        locationB.setLongitude(myLocation.getLongitude());

        float distance = locationA.distanceTo(locationB)/1000;

        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng( latitude_state,longitude_state));
        marker.title(getResources().getString(R.string.distamce) + distance + " km");

        map.addMarker(marker).showInfoWindow();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude_state, longitude_state), 2);
        map.animateCamera(cameraUpdate);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (map != null)
            setUpMap();
        if (map == null) {
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.google_map)).getMap();

            if (map !=null)
                setUpMap();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveButton.setVisibility(View.VISIBLE);
      //  slideButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (map!=null)
            getActivity().getFragmentManager().beginTransaction()
                    .remove(getActivity().getFragmentManager().findFragmentById(R.id.google_map))
                    .commit();
        map=null;

    }

}
