package com.asana.hbl.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asana.hbl.R;
import com.asana.hbl.utils.HBLImageLoader;
import com.asana.hbl.utils.Utils;
import com.asana.hbl.views.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gary on 2018/10/14.
 */

public class PlayByPlayFragment extends Fragment {
    public static final String EXTRA_TEXT = "arg";
    public static final String TAG = "HBL-PlayByPlayFragment";
    private ListPopupWindow mListPop;
    TextView mTv1;
    ListView mListView;
    RelativeLayout mDropMenu;
    private ImageView mDreoDownIndicator;

    public static PlayByPlayFragment newInsTance(){
        PlayByPlayFragment fragment = new PlayByPlayFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_by_play, container, false);
        mListView = (ListView)view.findViewById(R.id.lv);
        initDropMenu(view);
        initListView(null);
        return view;
    }

    private void initDropMenu(View view){
        final List<String > list = new ArrayList<String>();
        list.add("option1");
        list.add("option2");
        list.add("option3");

        mDropMenu = (RelativeLayout)view.findViewById(R.id.drop_menu);
        mDreoDownIndicator = (ImageView)view.findViewById(R.id.iv1);
        mTv1 = (TextView)view.findViewById(R.id.tv1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.expandable_list_item, list);
        mListPop = new ListPopupWindow(getActivity());
        mListPop.setAdapter(adapter);
        mListPop.setWidth(Utils.measureContentWidth(getContext(), adapter));
        mListPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mListPop.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.white)));
        mListPop.setAnchorView(mTv1);
        mListPop.setModal(true);
        mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTv1.setText(list.get(position));
                initListView(null);
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

        mDropMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDreoDownIndicator.animate()
                        .setDuration(Utils.ROTATION_ANIM_DURATION)
                        .rotationBy(180)
                        .start();
                mListPop.show();
            }
        });
        mTv1.setText(list.get(0));
    }

    private void initListView(JSONArray data){
        mListView.setAdapter(new CusomListAdapter(getContext(), data));
    }

    public class CusomListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<JSONObject> mListData= new ArrayList<JSONObject>();
        private class ViewHolder {
            TextView tv1,tv2, tv3, tv4;
            CircularImageView iv_player;
            public ViewHolder(TextView txt1, TextView txt2, TextView txt3, TextView txt4, CircularImageView iv){
                this.tv1 = txt1;
                this.tv2 = txt2;
                this.tv3 = txt3;
                this.tv4 = txt4;
                this.iv_player = iv;
            }
        }

        public CusomListAdapter(Context context, JSONArray data){
            //initDataList(data);
            mInflater = LayoutInflater.from(context);
        }

        private void initDataList(JSONArray data){
            for(int i=0; i<data.length(); i++){
                try {
                    JSONObject jsonObject = (JSONObject)data.get(i);
                    mListData.add((JSONObject)data.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getCount() {
            return 10;
            //return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
            //return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView==null){
                convertView = mInflater.inflate(R.layout.play_by_play_list_item, null);
                holder = new ViewHolder(
                        (TextView) convertView.findViewById(R.id.tv1),
                        (TextView) convertView.findViewById(R.id.tv2),
                        (TextView) convertView.findViewById(R.id.tv3),
                        (TextView) convertView.findViewById(R.id.tv4),
                        (CircularImageView) convertView.findViewById(R.id.iv)
                );
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            if(position%2 == 0){
                holder.tv3.setText("瘦子受歡迎");
                holder.tv3.setTextColor(Color.parseColor("#FF000A"));
            }else{
                holder.tv3.setText("胖子比較棒");
                holder.tv3.setTextColor(Color.parseColor("#0078FF"));
            }

            HBLImageLoader.loadPlayerImage(getActivity().getApplicationContext(), "", holder.iv_player);
            /*
            try {
                holder.tv_team_name.setText(mListData.get(position).getString("team_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                holder.tv_win.setText(mListData.get(position).getString("win_count"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                holder.tv_lose.setText(mListData.get(position).getString("loss_count"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                holder.tv_raking.setText(mListData.get(position).getString("rank"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                mImageLoader.get(mListData.get(position).getString("logo"), ImageLoader.getImageListener(holder.iv_team,
                        R.mipmap.team_default, R.mipmap.team_default));
                holder.iv_team.setImageUrl(mListData.get(position).getString("logo"), mImageLoader);
            } catch (JSONException e) {
                e.printStackTrace();
            }
           */
            return convertView;
        }
    }
}
