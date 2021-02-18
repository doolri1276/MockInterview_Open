package com.ub_hackers.mockinterview.activities.signinup;

import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.ub_hackers.mockinterview.R;
import com.ub_hackers.mockinterview.base.BaseActivity;
import com.ub_hackers.mockinterview.custom.CustomDialog;
import com.ub_hackers.mockinterview.tools.Logger;
import com.ub_hackers.mockinterview.tools.Utils;
import org.jetbrains.annotations.NotNull;
public class ChangePasswordActivity extends BaseActivity {
    private final String TAG = "ChangePasswordActivity";
    TextView tvChangePassword, tvCurPsw, tvWarPsw, tvWarPsw2;
    EditText etCpw, etPsw, etPsw2;

    String currentPassword="", psw1="", psw2="";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private boolean passwordChecker = false;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setViews();
        setListeners();
        setFirebase();
        setKeyboardSetting(this);
        setAppBar();
        setBack();
        tvChangePassword.setEnabled(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        currentPassword=etCpw.getText().toString();
        psw1=etPsw.getText().toString();
        psw2=etPsw2.getText().toString();
    }

    @Override
    public void setViews() {
        tvChangePassword = findViewById(R.id.tv_changePassword);
        etCpw=findViewById(R.id.et_currentPassword);
        etPsw=findViewById(R.id.et_psw);
        etPsw2=findViewById(R.id.et_psw2);
        tvCurPsw=findViewById(R.id.tv_war_currentPassword);
        tvWarPsw=findViewById(R.id.tv_war_psw);
        tvWarPsw2=findViewById(R.id.tv_war_psw2);
    }

    @Override
    public void setListeners(){
        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCurrentPassword();
            }
        });
        etCpw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentPassword = s.toString();
                checkEnabled();
                tvCurPsw.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                psw1 = s.toString();

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
                psw2 = s.toString();

                checkEnabled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkEnabled(){
        boolean enabled = true;
        if(currentPassword.length()<8 || psw1.length()<8|| psw2.length()<8){
            enabled = false;
        }
        if (psw1.length()>0 && !Utils.isValidPassword(psw1)){
            enabled = false;
            Logger.error(TAG,"bbb");
            tvWarPsw.setVisibility(View.VISIBLE);
        } else {
            tvWarPsw.setVisibility(View.INVISIBLE);
        }
        if (!psw1.equals(psw2)){
            enabled = false;
            Logger.error(TAG, "ccc");
            tvWarPsw2.setVisibility(View.VISIBLE);
        } else{
            tvWarPsw2.setVisibility(View.INVISIBLE);
        }

        tvChangePassword.setEnabled(enabled);
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
                        ChangePasswordActivity.super.onBackPressed();
                    }
                })
                .setCanceledOnTouchOutside(true)
                .create().show();

    }

    public void checkCurrentPassword(){
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(psw1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Logger.log(TAG, "User password updated");
                                final CustomDialog d = new CustomDialog(ChangePasswordActivity.this);
                                d
                                        .setTitle("Password changed")
                                        .setMessage("Your password is changed.")
                                        .setCanceledOnTouchOutside(false)
                                        .setRight("Okay", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                d.dialog.dismiss();
                                                finish();
                                            }
                                        })
                                        .setDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                d.dialog.dismiss();
                                                finish();
                                            }
                                        }).create().show();
                            }
                        }
                    });
                }else{
                    tvChangePassword.setEnabled(false);
                    tvCurPsw.setText("Wrong password");
                    tvCurPsw.setVisibility(View.VISIBLE);
                }
                passwordChecker = true;
                Logger.log(TAG,"User re-authenticated");
            }
        });
    }

}
