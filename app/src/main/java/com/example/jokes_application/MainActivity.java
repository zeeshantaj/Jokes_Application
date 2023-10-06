package com.example.jokes_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.jokes_application.Adapter.ViewPagerAdapter;
import com.example.jokes_application.Fragment.HomeFragment;
import com.example.jokes_application.Fragment.NotificationFragment;
import com.example.jokes_application.Fragment.ProfileFragment;
import com.example.jokes_application.Fragment.RequestFragment;
import com.example.jokes_application.FragmentUtils.FragmentUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        ViewPager viewPager = findViewById(R.id.myViewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new HomeFragment());
        viewPagerAdapter.addFragment(new RequestFragment());
        viewPagerAdapter.addFragment(new NotificationFragment());
        viewPagerAdapter.addFragment(new ProfileFragment());

        viewPager.setAdapter(viewPagerAdapter);

        navigationView = findViewById(R.id.bottomNavigation);

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.homeIcon) {
                    //FragmentUtils.setFragment(manager, R.id.mainFrameLayout, new HomeFragment());
                    viewPager.setCurrentItem(0, true);
                    return true;
                }
                if (id == R.id.requestIcon) {
                    //FragmentUtils.setFragment(manager, R.id.mainFrameLayout, new RequestFragment());
                    viewPager.setCurrentItem(1, true);
                    return true;
                }
                if (id == R.id.notificationIcon) {
                    // FragmentUtils.setFragment(manager, R.id.mainFrameLayout, new NotificationFragment());
                    viewPager.setCurrentItem(2, true);
                    return true;
                }
                if (id == R.id.profileIcon) {
                    //FragmentUtils.setFragment(manager, R.id.mainFrameLayout, new ProfileFragment());
                    viewPager.setCurrentItem(3, true);
                    return true;
                }
                return false;


            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigationView.getMenu().getItem(position).setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

}