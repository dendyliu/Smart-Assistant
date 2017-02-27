package org.kdadev.smartassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private LatLng home = new LatLng(-6.881468, 107.619537);

    private Location mLastLocation;
    private LatLng myLocation = home;

    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_location, container, false);

        final Button button = (Button) v.findViewById(R.id.showHome);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                showHome(v);
            }
        });

        final Button button1 = (Button) v.findViewById(R.id.showMyLocation);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                showMyLocation(v);
            }
        });

        final Button button2 = (Button) v.findViewById(R.id.goHome);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                goHome(v);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
/*        mMap =((SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMapAsync();*/
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        //        getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

/*        mMap = mapFragment.getMap;
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(home).title("Albert's"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(home, 18));*/

        //mapFragment.getMapAsync(this);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(home).title("Albert's"));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(home, 18));
    }

    private void showHome(View v) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(home, 18));
    }

    private void showMyLocation(View v) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 18));
    }

    private void goHome(View v) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + home.latitude + "," + home.longitude);
        Log.v("Intent:", gmmIntentUri.toString());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
/*        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);*/
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.v("No izin:",String.valueOf(myLocation.latitude));

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
<<<<<<< HEAD

=======
>>>>>>> 7a85fa2edc6e776b8e163ceae31da5da552613c6

        if (mLastLocation != null) {
            Log.v("Location:",String.valueOf(mLastLocation.getLatitude()));
            myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));
        }
    }

    protected void startLocationUpdates(){

    }

    @Override
    public void onConnectionFailed(ConnectionResult result){

    }

    @Override
    public void onConnectionSuspended(int cause){

    }

   public LatLng getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(getContext());
        List<android.location.Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            android.location.Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng((double) (location.getLatitude() * 1E6),
                    (double) (location.getLongitude() * 1E6));

            return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
       return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String name_sett = prefs.getString("address", null);
        if(name_sett!=null){
            LatLng x = getLocationFromAddress(name_sett);
            if(x!=null){
                home=x;
            }
        }
    }
}
