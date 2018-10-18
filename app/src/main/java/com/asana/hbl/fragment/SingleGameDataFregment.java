package com.asana.hbl.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asana.hbl.R;
import com.asana.hbl.model.HBLSingleGameData;
import com.asana.hbl.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gary on 2018/10/4.
 */

public class SingleGameDataFregment extends Fragment {
    public static final String EXTRA_TEXT = "arg";
    public static final String TAG = "HBL-SingleGameDataFregment";
    private RelativeLayout mTeamSpinner;
    private List<String > mList = new ArrayList<String>();
    private TextView mTitle;
    private ListPopupWindow mListPop;
    private HBLSingleGameData mHBLSingleGameData;
    private ImageView mDreoDownIndicator;
    public static SingleGameDataFregment newInsTance(){
        SingleGameDataFregment fragment = new SingleGameDataFregment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_game_data, container, false);
        mTeamSpinner = (RelativeLayout)view.findViewById(R.id.spinner_teams);
        mTitle = (TextView)view.findViewById(R.id.titles);
        mDreoDownIndicator = (ImageView)view.findViewById(R.id.indicator);
        mTeamSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDreoDownIndicator.animate()
                        .setDuration(Utils.ROTATION_ANIM_DURATION)
                        .rotationBy(180)
                        .start();
                mListPop.show();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            try {
                mHBLSingleGameData = new HBLSingleGameData(new JSONObject(bundle.getString(EXTRA_TEXT)));
                mList.add(mHBLSingleGameData.home_name);
                mList.add(mHBLSingleGameData.away_name);
                initDropMenu();
                mTitle.setText(mList.get(0));
                loadTeamData(mHBLSingleGameData.getDataInString(), 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void initDropMenu(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.expandable_list_item, mList);
        mListPop = new ListPopupWindow(getActivity());
        mListPop.setAdapter(adapter);
        mListPop.setWidth(Utils.measureContentWidth(getContext(), adapter));
        mListPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mListPop.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.white)));
        mListPop.setAnchorView(mTitle);
        mListPop.setModal(true);
        mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTitle.setText(mList.get(position));
                loadTeamData(mHBLSingleGameData.getDataInString(), position);
                mListPop.dismiss();
            }
        });
        mListPop.setOnDismissListener(new PopupWindow.OnDismissListener(){
            @Override
            public void onDismiss() {
                mDreoDownIndicator.animate()
                        .setDuration(Utils.ROTATION_ANIM_DURATION)
                        .rotationBy(180)
                        .start();
            }
        });
    }

    private void loadTeamData(String data, int selectedTeam){
        TeamDataFragment fragment = new TeamDataFragment().newInsTance(selectedTeam);
        getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction beginTransaction = getChildFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString("arg", data);
        fragment.setArguments(args);
        beginTransaction.addToBackStack(null)
                .add(R.id.content, fragment, TeamDataFragment.TAG)
                .commit();
    }
}
