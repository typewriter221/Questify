package in.shriyansh.streamify.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import in.shriyansh.streamify.R;
import in.shriyansh.streamify.activities.MainActivity;
import in.shriyansh.streamify.utils.PreferenceUtils;

import static android.graphics.BitmapFactory.*;
import static in.shriyansh.streamify.utils.PreferenceUtils.PREF_USER_ROLL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Dashboard.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Dashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dashboard extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView roll;
    private TextView year;
    private TextView branch;
    private TextView contact;
    private TextView name;
    private TextView email;
    private ImageView profilepic;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Dashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static Dashboard newInstance(String param1, String param2) {
        Dashboard fragment = new Dashboard();
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
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        roll = view.findViewById(R.id.dashboard_roll);
        year = view.findViewById(R.id.dashboard_year);
        contact = view.findViewById(R.id.dashboard_contact);
        email = view.findViewById(R.id.dashboard_email);
        branch = view.findViewById(R.id.dashboard_branch);
        name = view.findViewById(R.id.dashboard_name);
        profilepic = view.findViewById(R.id.pofilepic);

        roll.setText(PreferenceUtils.getStringPreference(getActivity(), PreferenceUtils.PREF_USER_ROLL));
        name.setText(PreferenceUtils.getStringPreference(getActivity(), PreferenceUtils.PREF_USER_NAME));
        email.setText(PreferenceUtils.getStringPreference(getActivity(), PreferenceUtils.PREF_USER_EMAIL));
        year.setText(PreferenceUtils.getStringPreference(getActivity(), PreferenceUtils.PREF_USER_YEAR_JOIN));
        branch.setText(PreferenceUtils.getStringPreference(getActivity(), PreferenceUtils.PREF_USER_BRANCH));
        contact.setText(PreferenceUtils.getStringPreference(getActivity(), PreferenceUtils.PREF_USER_CONTACT));
        setProfilePic(profilepic);



        return view;
    }


    public static final String TAG = "SampleActivity";

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setProfilePic(ImageView profilePic) {
        try {
            File f=new File(getActivity().getApplicationContext().getFilesDir().getPath(), "profile.jpg");
            Bitmap b = decodeStream(new FileInputStream(f));
            profilePic.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
