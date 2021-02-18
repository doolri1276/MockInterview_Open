package com.ub_hackers.mockinterview.activities;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ub_hackers.mockinterview.Go;
import com.ub_hackers.mockinterview.R;
import com.ub_hackers.mockinterview.activities.signinup.SignInActivity;
import com.ub_hackers.mockinterview.base.BaseActivity;
import com.ub_hackers.mockinterview.models.User;
import com.ub_hackers.mockinterview.singletons.LoginedUser;
import com.ub_hackers.mockinterview.tools.Logger;
import org.jetbrains.annotations.NotNull;

public class SplashActivity extends BaseActivity {

    private final String TAG="SplashActivity";

    RelativeLayout rlLogo;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    boolean isLogined;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setViews();
        startAnimation();
        setFirebase();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            isLogined=true;
            mDatabase=FirebaseDatabase.getInstance().getReference();
            mDatabase.child("users").child(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                    User u = new User(task.getResult().child("email").getValue().toString(), task.getResult().child("username").getValue().toString());
                    LoginedUser.getInstance().signIn(currentUser.getUid(), u);
                    Go.goToMainActivity(SplashActivity.this);
                }
            });
        }
    }

    @Override
    public void setViews() {
        rlLogo = findViewById(R.id.rl_logo);
    }

    @Override
    public void setFirebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void startAnimation(){


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_show);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rlLogo.setVisibility( View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(isLogined)
                    Go.goToMainActivity(SplashActivity.this);
                else
                    Go.goToSignInActivity(SplashActivity.this);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rlLogo.startAnimation(animation);
    }
}