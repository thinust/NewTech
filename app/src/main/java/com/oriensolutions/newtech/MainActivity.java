package com.oriensolutions.newtech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    MeowBottomNavigation bottomNavigation;
    RelativeLayout main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_layout = findViewById(R.id.main_layout);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        replace(new HomeFragment());

        bottomNavigation.show(2, true);

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.order_icon));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.home_icon));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.profile_icon));

//        main_layout.setBackgroundColor(Color.parseColor("#FFFFFFFF"));

        meownavigation();

    }

    public void meownavigation() {
        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case 1:
                        replace(new OrderFragment());
                        break;
                    case 2:
                        replace(new HomeFragment());

                        break;
                    case 3:
                        replace(new ProfileFragment());

                        break;
                }
                return null;
            }
        });
    }


    public void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();


    }
}