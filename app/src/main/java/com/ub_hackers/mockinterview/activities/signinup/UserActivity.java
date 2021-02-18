package com.ub_hackers.mockinterview.activities.signinup;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ub_hackers.mockinterview.Go;
import com.ub_hackers.mockinterview.R;
import com.ub_hackers.mockinterview.activities.MainActivity;
import com.ub_hackers.mockinterview.base.BaseActivity;
import com.ub_hackers.mockinterview.custom.CustomDialog;
import com.ub_hackers.mockinterview.singletons.LoginedUser;
import com.ub_hackers.mockinterview.tools.Utils;

public class UserActivity extends BaseActivity {

    LinearLayout llPsw, llReset;
    TextView tvUserName, tvLogout;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setViews();
        setListeners();
        setAppBar();
        setData();
        setFirebase();
    }

    @Override
    public void setFirebase() {
        super.setFirebase();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void setAppBar() {
        super.setAppBar();
        setWhiteBack();
    }

    @Override
    public void setViews() {
        llPsw = findViewById(R.id.ll_psw);
        llReset = findViewById(R.id.ll_reset);
        tvUserName = findViewById(R.id.tv_username);
        tvLogout = findViewById(R.id.tv_logout);
    }

    @Override
    public void setListeners() {
        super.setListeners();

        llPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Go.goToChangePasswordActivity(UserActivity.this);
            }
        });

        llReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void reset(){
        final CustomDialog dialog = new CustomDialog(this);
        dialog
                .setTitle("Are you sure you want to reset?")
                .setMessage("This action cannot be undone.")
                .setRight("Reset", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatabase.child("users").child(LoginedUser.getInstance().getUserPk()).child("questions").setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dialog.dismiss();
                            }
                        });
                    }
                }).setLeft("Cancel", null)
                .setCanceledOnTouchOutside(true)
                .create().show();
    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        LoginedUser.getInstance().signOut();
        Go.goToSignInActivity(UserActivity.this);
    }

    private void setData(){
        tvUserName.setText("@"+ LoginedUser.getInstance().getUser().username);
    }
}