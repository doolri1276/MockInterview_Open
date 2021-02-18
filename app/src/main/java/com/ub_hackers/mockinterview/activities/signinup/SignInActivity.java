package com.ub_hackers.mockinterview.activities.signinup;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ub_hackers.mockinterview.Go;
import com.ub_hackers.mockinterview.R;
import com.ub_hackers.mockinterview.activities.SplashActivity;
import com.ub_hackers.mockinterview.base.BaseActivity;
import com.ub_hackers.mockinterview.models.User;
import com.ub_hackers.mockinterview.singletons.LoginedUser;
import com.ub_hackers.mockinterview.tools.Logger;
import com.ub_hackers.mockinterview.tools.Utils;
import org.jetbrains.annotations.NotNull;

public class SignInActivity extends BaseActivity {

    private final String TAG="SignInActivity";

    private FirebaseAuth mAuth;

    TextView tvSignIn,tvSignUp, tvWarEmail, tvWarPsw;
    EditText etEmail, etPsw;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setViews();
        setListeners();
        setPressAgainToClose();
        setFirebase();
        setKeyboardSetting(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkEnabled();
    }

    @Override
    public void setFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void setViews() {
        tvSignIn=findViewById(R.id.tv_signin);
        tvSignUp=findViewById(R.id.tv_signup);
        etEmail=findViewById(R.id.et_cpw);
        etPsw=findViewById(R.id.et_psw);
        tvWarEmail=findViewById(R.id.tv_war_cpw);
        tvWarPsw=findViewById(R.id.tv_war_psw);
    }

    @Override
    public void setListeners() {
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Go.goToSignUpActivity(SignInActivity.this);
            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvWarPsw.setVisibility(View.INVISIBLE);
                if(Utils.isValidEmail(s.toString())) {
                    tvWarEmail.setVisibility(View.INVISIBLE);
                }else {
                    tvWarEmail.setVisibility(View.VISIBLE);
                }

                checkEnabled();
            }
        });

        etPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvWarPsw.setVisibility(View.INVISIBLE);
                checkEnabled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void checkEnabled() {
        boolean enabled = true;
        if(!Utils.isValidEmail(etEmail.getText().toString())) {
            enabled=false;
        }else if(!Utils.isValidPassword(etPsw.getText().toString()))
            enabled=false;

        tvSignIn.setEnabled(enabled);

    }

    private void signIn(){
        Utils.keyboardHide(this);
        Logger.error(TAG, "email : "+etEmail.getText().toString()+", psw : "+etPsw.getText().toString());

        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPsw.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            final FirebaseUser user = mAuth.getCurrentUser();

                            mDatabase.child("users").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {

                                    User u = new User(task.getResult().child("email").getValue().toString(), task.getResult().child("username").getValue().toString());
                                    LoginedUser.getInstance().signIn(user.getUid(), u);

                                    Go.goToMainActivity(SignInActivity.this);
                                }
                            });

                        }else{
                            tvWarPsw.setVisibility(View.VISIBLE);
                        }
                    }
                });


    }
}