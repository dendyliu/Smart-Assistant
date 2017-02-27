package org.kdadev.smartassistant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.kdadev.smartassistant.data.WeatherAPIService;
import org.kdadev.smartassistant.model.WeatherForecast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class  WeatherFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Call<WeatherForecast> responseCall;

    private final String APIKey = "298ae148b6cfd043e5acc27c21977021";
    private OnFragmentInteractionListener mListener;

    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    private void setCurrentWeather(double latitude, double longitude, final View V){
        WeatherForecast body;
        responseCall = WeatherAPIService.service.getWeather(latitude, longitude, "metric", APIKey);
        responseCall.enqueue(new Callback<WeatherForecast>() {
            @Override
            public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                if (response.isSuccessful()){
                    Format formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                    String today = formatter.format(new Date());
                    TextView currTime = (TextView) V.findViewById(R.id.currTime);
                    WeatherForecast weather = response.body();
                    TextView temp = (TextView) V.findViewById(R.id.TempCurrent);
                    TextView minTemp = (TextView) V.findViewById(R.id.minTempCurrent);
                    ImageView img = (ImageView) V.findViewById(R.id.weatherCurrent);
                    TextView currLoc = (TextView) V.findViewById(R.id.CurrentLocation);
                    Picasso.with(getContext()).load("http://openweathermap.org/img/w/"+weather.getWeather().get(0).getIcon()+".png").into(img);
                    TextView status = (TextView) V.findViewById(R.id.StatusCurrent);
                    currTime.setText(today);
                    currLoc.setText(weather.getName()+", " +weather.getSys().getCountry());
                    temp.setText(Long.toString(Math.round(weather.getMain().getTemp()))+"°");
                    minTemp.setText(Long.toString(Math.round(weather.getMain().getTempMin()))+"°");
                    status.setText(weather.getWeather().get(0).getDescription());
                }
            }

            @Override
            public void onFailure(Call<WeatherForecast> call, Throwable t) {
                Log.e("ERROR WEATHER", t.getMessage());
            }

        });
    }

    private void setHomeWeather(double latitude, double longitude, final View V){
        WeatherForecast body;
        responseCall = WeatherAPIService.service.getWeather(latitude, longitude, "metric", APIKey);
        responseCall.enqueue(new Callback<WeatherForecast>() {
            @Override
            public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                if (response.isSuccessful()){
                    WeatherForecast weather = response.body();
                    TextView temp = (TextView) V.findViewById(R.id.TempHome);
                    TextView minTemp = (TextView) V.findViewById(R.id.minTempHome);
                    ImageView img = (ImageView) V.findViewById(R.id.weatherHome);
                    TextView status = (TextView) V.findViewById(R.id.StatusHome);
                    temp.setText(Long.toString(Math.round(weather.getMain().getTemp()))+"°");
                    Picasso.with(getContext()).load("http://openweathermap.org/img/w/"+weather.getWeather().get(0).getIcon()+".png").into(img);
                    minTemp.setText(Long.toString(Math.round(weather.getMain().getTempMin()))+"°");
                    status.setText(weather.getWeather().get(0).getDescription());
                    Log.v("weather", weather.getMain().getTemp().toString());
                }
            }

            @Override
            public void onFailure(Call<WeatherForecast> call, Throwable t) {
                Log.e("ERROR WEATHER", t.getMessage());
            }

        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_weather, container, false);
        setCurrentWeather(10,10,v);
        setHomeWeather(20,20,v);
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
}
