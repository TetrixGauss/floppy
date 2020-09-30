package com.ath.floppy;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ath.floppy.util.UIUtil;
import com.parse.Parse;
import com.parse.ParseUser;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("qPhskQQKyN2WEsVZp3D2a7pRR3lWQWpoj6OW7KLU")
                        .clientKey("yYtBBPwEEJffqT3IGUKn9yFWVlXAnZrEJwoy55sB")
                        .server("https://parseapi.back4app.com")
                        .build()
        );
        getCurrentUser();

        ViewPager viewPager = findViewById(R.id.viewPager);

        AuthenticationPagerAdapter pagerAdapter = new AuthenticationPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragmet(new LoginFragment());
        pagerAdapter.addFragmet(new RegisterFragment());
        viewPager.setAdapter(pagerAdapter);
    }

    class AuthenticationPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        public AuthenticationPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragmet(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }

//    public void showProgress(final boolean show) {
//        UIUtil.showProgress(this, progressView, fragmentContainer, show);
//    }

    public void getCurrentUser() {
        final Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            startActivity(intent);
        } else {
            // show the signup or login screen
        }
    }
}


