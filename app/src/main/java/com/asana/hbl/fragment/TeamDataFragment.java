package com.asana.hbl.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.asana.hbl.R;
import com.asana.hbl.model.ExpandableListAdapter;
import com.asana.hbl.model.HBLSingleGameData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gary on 2018/10/5.
 */

public class TeamDataFragment extends Fragment {
    public static final String EXTRA_TEXT = "arg";
    public static final String TAG = "HBL-TeamDataFragment";

    private static final int TEAM_HOME = 0;
    private static final int TEAM_AWAY = 1;
    private TextView mTextView;
    private static int selectedTeam = 0;
    private HBLSingleGameData mHBLSingleGameData;
    private ExpandableListAdapter mListAdapter;
    private ExpandableListView mExpListView;

    public static TeamDataFragment newInsTance(){
        TeamDataFragment fragment = new TeamDataFragment();
        return fragment;
    }

    public static TeamDataFragment newInsTance(int selected){
        selectedTeam = selected;
        TeamDataFragment fragment = new TeamDataFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_data, container, false);
        mExpListView = (ExpandableListView)view.findViewById(R.id.lvExp);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            try {
                mHBLSingleGameData = new HBLSingleGameData(new JSONObject(bundle.getString(EXTRA_TEXT)));
                switch(selectedTeam){
                    case TEAM_HOME:
                        Log.v(TAG, "Slected teah : TEAM_HOME");
                        mHBLSingleGameData.setSelectedTeam(HBLSingleGameData.TEAM_HOME);
                        break;
                    case TEAM_AWAY:
                        Log.v(TAG, "Slected teah : TEAM_AWAY");
                        mHBLSingleGameData.setSelectedTeam(HBLSingleGameData.TEAM_AWAY);
                        break;
                }
                initListView(mHBLSingleGameData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initListView(HBLSingleGameData data){
        mListAdapter = new ExpandableListAdapter(getContext(), mHBLSingleGameData);
        mExpListView.setAdapter(mListAdapter);
    }
}
