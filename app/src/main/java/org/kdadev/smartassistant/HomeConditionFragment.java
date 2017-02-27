package org.kdadev.smartassistant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.kdadev.smartassistant.data.MyHomeConditionCollectorService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeConditionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeConditionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeConditionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Intent intent;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeConditionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeConditionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeConditionFragment newInstance(String param1, String param2) {
        HomeConditionFragment fragment = new HomeConditionFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_home_condition, container, false);
        intent = new Intent(getContext(), MyHomeConditionCollectorService.class);
        return v;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getContext().startService(intent);
        getContext().registerReceiver(broadcastReceiver, new IntentFilter(MyHomeConditionCollectorService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(broadcastReceiver);
        getContext().stopService(intent);
    }

    private void updateUI(Intent intent) {
        String tempNow = String.valueOf(intent.getIntExtra("temp",0)) + " °";
        String humidNow = String.valueOf(intent.getIntExtra("humid",0)) + "mmHg";
        String brightNow = String.valueOf(intent.getIntExtra("bright",0)) + " lux";
        TextView mTemp = (TextView) getView().findViewById(R.id.temp_condition_home);
        TextView mHumid = (TextView) getView().findViewById(R.id.humid_condition_home);
        TextView mBright = (TextView) getView().findViewById(R.id.bright_condition_home);
        mTemp.setText(tempNow);
        mHumid.setText(humidNow);
        mBright.setText(brightNow);
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
