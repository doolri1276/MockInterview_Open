package com.ub_hackers.mockinterview.activities.signinup;

import android.provider.SyncStateContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.ub_hackers.mockinterview.Go;
import com.ub_hackers.mockinterview.R;
import com.ub_hackers.mockinterview.base.BaseActivity;
import com.ub_hackers.mockinterview.custom.CustomDialog;
import com.ub_hackers.mockinterview.models.User;
import com.ub_hackers.mockinterview.singletons.LoginedUser;
import com.ub_hackers.mockinterview.tools.Logger;
import com.ub_hackers.mockinterview.tools.Utils;
import org.jetbrains.annotations.NotNull;

public class SignUpActivity extends BaseActivity {
    private final String TAG="SignUpActivity";
    TextView tvSignUp, tvWarEmail, tvWarUserName, tvWarPsw, tvWarPsw2;
    EditText etEmail, etUserName, etPsw, etPsw2;

    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    String email,userName,psw1,psw2;

    private boolean usernameChecker = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setViews();
        setListeners();
        setFirebase();
        setKeyboardSetting(this);
        setAppBar();
        setBack();
        usernameExists();
    }

    @Override
    protected void onResume() {
        super.onResume();
        email=etEmail.getText().toString();
        userName=etUserName.getText().toString();
        psw1=etPsw.getText().toString();
        psw2=etPsw2.getText().toString();
        checkEnabled();
    }

    @Override
    public void setViews() {
        tvSignUp = findViewById(R.id.tv_signup);
        etEmail=findViewById(R.id.et_cpw);
        etUserName=findViewById(R.id.et_username);
        etPsw=findViewById(R.id.et_psw);
        etPsw2=findViewById(R.id.et_psw2);
        tvWarEmail=findViewById(R.id.tv_war_cpw);
        tvWarUserName=findViewById(R.id.tv_war_username);
        tvWarPsw=findViewById(R.id.tv_war_psw);
        tvWarPsw2=findViewById(R.id.tv_war_psw2);
    }

    @Override
    public void setListeners() {
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email=s.toString();
                Logger.warn(TAG, "email : " + email);
                checkEnabled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userName=s.toString();
                usernameExists();
                checkEnabled();

            }
        });

        etPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                psw1=s.toString();

                checkEnabled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPsw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                psw2=s.toString();

                checkEnabled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void setFirebase() {
        mAuth=FirebaseAuth.getInstance();

    }

    private void checkEnabled() {
        boolean enabled=true;
        if(!Utils.isValidEmail(email)) {
            if(email.length()>0){
                tvWarEmail.setText("Please enter a valid email address.");
                tvWarEmail.setVisibility(View.VISIBLE);
            }
            enabled=false;
        }else
            tvWarEmail.setVisibility(View.INVISIBLE);

        if(userName==null|| userName.length()==0) enabled=false;
        else if(usernameChecker){
            tvWarUserName.setText("This username is taken.");
            tvWarUserName.setVisibility(View.VISIBLE);
            enabled=false;
        } else
            tvWarUserName.setVisibility(View.INVISIBLE);

        if(!Utils.isValidPassword(psw1)) {
            enabled = false;
            if(psw1.length()>0)
                tvWarPsw.setVisibility(View.VISIBLE);
        }else
            tvWarPsw.setVisibility(View.INVISIBLE);

        if(!psw1.equals(psw2)) {
            enabled = false;
            if(psw2.length()>0){
                tvWarPsw2.setVisibility(View.VISIBLE);
            }
        }else
            tvWarPsw2.setVisibility(View.INVISIBLE);

        tvSignUp.setEnabled(enabled);


    }

    private void signUp(){
        Utils.keyboardHide(this);
        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPsw.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.getException()!=null)
                            task.getException().getStackTrace();

                        if(task.isSuccessful()){
                            mDatabase= FirebaseDatabase.getInstance().getReference();
                            User user = new User(email, userName);
                            LoginedUser.getInstance().signIn(mAuth.getCurrentUser().getUid(), user);

                            mDatabase.child("users").child(LoginedUser.getInstance().getUserPk()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Logger.error(TAG,"firebase uploaded");
                                        Go.goToMainActivity(SignUpActivity.this);
                                    }else
                                        task.getException().printStackTrace();
                                }
                            });

                        }else{
                            Logger.error(TAG, "createUserWithEmail:failure"+task.getException());
                            tvSignUp.setEnabled(false);
                            tvWarEmail.setText("This email has already been taken.");
                            tvWarEmail.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        final CustomDialog dialog = new CustomDialog(this);
        dialog
                .setTitle("Are you sure you want to exit?")
                .setMessage("Changes you made may not be saved")
                .setLeft("Cancel", null)
                .setRight("Exit", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dialog.dismiss();
                        SignUpActivity.super.onBackPressed();
                    }
                })
                .setCanceledOnTouchOutside(true)
                .create().show();

    }

    private void usernameExists(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.orderByChild("username").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    usernameChecker = true;
                } else{
                    usernameChecker = false;
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}