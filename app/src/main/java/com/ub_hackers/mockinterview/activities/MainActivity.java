package com.ub_hackers.mockinterview.activities;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.ub_hackers.mockinterview.Go;
import com.ub_hackers.mockinterview.R;
import com.ub_hackers.mockinterview.base.BaseActivity;
import com.ub_hackers.mockinterview.singletons.LoginedUser;

public class MainActivity extends BaseActivity {
    ImageView ivMenu;
    LinearLayout llItem1, llItem2, llItem3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        setListeners();
        setAppBar();
    }

    @Override
    public void setAppBar() {
        super.setAppBar();
        setTitle("HIGH RISE");
        setUser(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Go.goToUserActivity(MainActivity.this);
            }
        });

    }

    @Override
    public void setViews() {
        ivMenu=findViewById(R.id.iv_menu);
        llItem1=findViewById(R.id.ll_item1);
        llItem2=findViewById(R.id.ll_item2);
        llItem3=findViewById(R.id.ll_item3);
    }

    @Override
    public void setListeners() {
        llItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Go.goToInterviewMainActivity(MainActivity.this);
            }
        });
        llItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Go.goToAnswersActivity(MainActivity.this);
            }
        });
        llItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Go.goToTipsActivity(MainActivity.this);
            }
        });

    }

}