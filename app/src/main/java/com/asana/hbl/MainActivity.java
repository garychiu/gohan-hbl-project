package com.asana.hbl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.asana.hbl.activity.BaseActivity;
import com.asana.hbl.fragment.AnalysisFragment;
import com.asana.hbl.fragment.DashboardFragment;
import com.asana.hbl.fragment.GameFragment;

public class MainActivity extends BaseActivity {
    private TextView mTitle;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_games:
                    showNav(R.id.navigation_games);
                    return true;
                case R.id.navigation_dashboard:
                    showNav(R.id.navigation_dashboard);
                    return true;
                case R.id.navigation_analysis:
                    showNav(R.id.navigation_analysis);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
        initViews();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private void initActionBar(){
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflater = (LayoutInflater)ab.getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customActionBar = inflater.inflate( R.layout.activity_main_titlebar, null);
        ab.setCustomView(customActionBar);

        Toolbar toolbar = (Toolbar)ab.getCustomView().getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);
    }
    private void initViews(){
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        Fragment gameFragment = GameFragment.newInsTance();
        Fragment dashboardFragment = DashboardFragment.newInsTance();
        Fragment analysisFragment = AnalysisFragment.newInsTance();
        beginTransaction
                .add(R.id.content, gameFragment, GameFragment.TAG)
                .add(R.id.content, dashboardFragment, DashboardFragment.TAG)
                .add(R.id.content, analysisFragment, AnalysisFragment.TAG)
                .show(gameFragment)
                .hide(dashboardFragment)
                .hide(analysisFragment)
                .commit();
        mCurrentFragment = gameFragment;
        mTitle = (TextView) findViewById(R.id.title);
        //showNav(R.id.navigation_games);
    }
    Fragment mCurrentFragment;
    private void showNav(int navid){
        //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        Fragment targetFragment = null;

        switch (navid){
            case R.id.navigation_games:
                if(mTitle != null) {
                    mTitle.setText(R.string.title_games);
                }
                targetFragment = getSupportFragmentManager().findFragmentByTag(GameFragment.TAG);
                if(targetFragment == null){
                    targetFragment = GameFragment.newInsTance();
                    beginTransaction
                            .add(R.id.content, targetFragment, GameFragment.TAG);

                }
                break;
            case R.id.navigation_dashboard:
                if(mTitle != null) {
                    mTitle.setText(R.string.title_dashboard);
                }
                targetFragment = getSupportFragmentManager().findFragmentByTag(DashboardFragment.TAG);
                if(targetFragment == null){
                    targetFragment = DashboardFragment.newInsTance();
                    beginTransaction
                            .add(R.id.content, targetFragment, DashboardFragment.TAG);

                }
                break;
            case R.id.navigation_analysis:
                if(mTitle != null) {
                    mTitle.setText(R.string.title_analysis);
                }
                targetFragment = getSupportFragmentManager().findFragmentByTag(AnalysisFragment.TAG);
                if(targetFragment == null){
                    targetFragment = AnalysisFragment.newInsTance();
                    beginTransaction
                            .add(R.id.content, targetFragment, AnalysisFragment.TAG);

                }
                break;
        }
        if(targetFragment != null) {
            if(mCurrentFragment != null){
                beginTransaction.hide(mCurrentFragment);
            }
            beginTransaction.show(targetFragment).commit();
            mCurrentFragment = targetFragment;
        }
    }

    @Override
    public void onBackPressed(){
        if(getFragmentManager().beginTransaction().isEmpty()){
            finish();
            return;
        }
        super.onBackPressed();
    }
}

