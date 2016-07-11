package com.youzh.tabswitchview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.switchTab)
    TabSwitchView mTab;
    @BindView(R.id.viewPager)
    ViewPager mPager;
    private ArrayList<Fragment> mFragmentsList = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFragmentsList.add(new FragmentOne());
        mFragmentsList.add(new FragmentTwo());
        mFragmentsList.add(new FragmentThree());
        mPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(), mFragmentsList, new String[]{"每日赚取", "持有投资", "到期退出"}));
        mTab.setViewPager(mPager);
    }
}
